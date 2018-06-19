package Model;

import Service.Constants;
import Service.Parameters;

import java.util.HashSet;
import java.util.Set;

class AI {
    private int player;
    private int side;
    private DotBuilder dot;
    private Set<Board> analized;

    AI(DotBuilder dot) {
        this.side = Parameters.size*2 - 1;
        this.dot = dot;
    }

    void setPlayer(int player) {
        this.player = player;
    }

    Turn getMove(Board board) {
        int opponent = player == 1 ? 2 : 1;
        Turn first = new Turn(opponent, Constants.WORSTVALUE, board.duplicate());
        first.setFirst();
        Turn bestMove;

        if(Parameters.maxTime != -1) {
            final long maxTime = Parameters.maxTime * 1000 + System.currentTimeMillis();
            TimeLimit timeLimit = () -> System.currentTimeMillis() > maxTime;

            int depth = 0;
            Turn move = first;

            do {
                dot.restart(player);
                bestMove = move;
                depth++;
                move = getMove(first,depth,timeLimit);
            } while (!timeLimit.exceeded());

        } else {
            TimeLimit timeLimit = () -> false;
            dot.restart(player);
            bestMove = getMove(first,Parameters.depth,timeLimit);
        }

        return bestMove;
    }

    private Turn getMove(Turn first, int depth, TimeLimit timeLimit) {
        if (Parameters.prune)
            return negamax(first,depth,Constants.WORSTVALUE,Constants.BESTVALUE,player,timeLimit);
        else
            return negamaxNoPrune(first,depth,player,timeLimit);
    }

    private Turn negamax(Turn move, int depth, int alpha, int beta, int player, TimeLimit timeLimit) {
        if (depth == 0 || move.getBoard().gameOver()) {
            move.setValue(ponderHeuristicValue(move,player));
            return move;
        }
        int opponent = player == 1 ? 2 : 1;

        Set<Turn> children = generateMoves(player,move.getBoard());

        Turn bestMove = new Turn(player,Constants.WORSTVALUE,move.getBoard().duplicate());
        for (Turn child : children) {
            if (timeLimit.exceeded())
                break;

            if (beta > alpha) {
                child.setValue(-negamax(child,depth-1,-beta,-alpha,opponent,timeLimit).getValue());
                if (child.getValue() > bestMove.getValue()) {
                    bestMove = child;
                }
                if (child.getValue() > alpha)
                    alpha = child.getValue();
            } else
                child.setPruned();

            dot.addEdge(move,child);
            dot.setLabel(child);
        }
        move.setValue(bestMove.getValue());
        dot.changeColor(bestMove);
        dot.setLabel(move);
        return bestMove;
    }

    private Turn negamaxNoPrune(Turn move, int depth, int player, TimeLimit timeLimit) {
        if (depth == 0 || move.getBoard().gameOver()) {
            move.setValue(ponderHeuristicValue(move,player));
            return move;
        }
        int opponent = player == 1 ? 2 : 1;

        Set<Turn> children = generateMoves(player,move.getBoard());

        Turn bestMove = new Turn(player,Constants.WORSTVALUE,move.getBoard().duplicate());
        for (Turn child : children) {
            if (timeLimit.exceeded())
                break;

            child.setValue(-negamaxNoPrune(child,depth-1,opponent,timeLimit).getValue());
            if (child.getValue() > bestMove.getValue())
                bestMove = child;

            dot.addEdge(move,child);
            dot.setLabel(child);
        }
        move.setValue(bestMove.getValue());
        dot.changeColor(bestMove);
        dot.setLabel(move);
        return bestMove;
    }

    private Set<Turn> generateMoves(int player, Board board) {

        analized = new HashSet<>();

        Set<Index> possibleIndexes = getPossibleIndexes(player, board.duplicate());

        Set<Turn> moves = new HashSet<>();

        generateMoves(player, new Turn(player,board.duplicate()), possibleIndexes, moves);

        return moves;
    }

    private Set<Index> getPossibleIndexes(int player, Board board) {
        Set<Index> possibleIndexes = new HashSet<>();
        for (int row=0; row<side; row++) {
            for (int col=0; col<Parameters.size; col++) {
                if (!(row%2==0 && col==Parameters.size-1))
                    if (board.verifyTurn(row,col,player))
                        possibleIndexes.add(new Index(row,col));
            }
        }
        return possibleIndexes;
    }

    private void generateMoves(int player, Turn move, Set<Index> possibleIndexes, Set<Turn> moves) {
        if (possibleIndexes.isEmpty())
            moves.add(move);

        for (Index index : possibleIndexes) {
            Turn aux = move.duplicate();
            if (aux.getBoard().verifyTurn(index.getRow(),index.getCol(),player)) {
                aux.addLine(index.getRow(),index.getCol());
                if (aux.getBoard().turnContinues()) {
                    if (!analized.contains(aux.getBoard())) {
                        analized.add(aux.getBoard());
                        Set<Index> indexesLeft = new HashSet<>(possibleIndexes);
                        indexesLeft.remove(index);
                        generateMoves(player,aux,indexesLeft,moves);
                    }
                } else {
                    moves.add(aux);
                }
            }
        }
    }

    private Index getFinalIndex(int row) {
        int col;
        if (row%2 == 0)
            col = Parameters.size - 2;
        else
            col = Parameters.size - 1;
        return new Index(row,col);
    }

    private int ponderHeuristicValue(Turn move, int player) {
        int opponent = player == 1 ? 2 : 1;
        Board board = move.getBoard();

        return board.getPlayerScore(player) - board.getPlayerScore(opponent);
    }
}