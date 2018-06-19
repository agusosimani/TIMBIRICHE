package Model;

import Service.Constants;
import Service.Parameters;

import java.util.HashSet;
import java.util.Set;

class AI {
    private int player;
    private Board board;
    private int side;
    private DotBuilder dot;

    AI(Board board, DotBuilder dot) {
        this.board = board;
        this.side = Parameters.size*2 - 1;
        this.dot = dot;
    }

    void setPlayer(int player) {
        this.player = player;
    }

    Turn getMove() {
        int opponent = player == 1 ? 2 : 1;
        Turn first = new Turn(player, Constants.WORSTVALUE, board.duplicate());
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
//            System.out.println(move.getBoard());
//            System.out.println(move);
            return move;
        }
        int opponent = player == 1 ? 2 : 1;

        Set<Turn> children = generateMoves(player,move.getBoard());

        Turn bestMove = new Turn(player,Constants.WORSTVALUE,board.duplicate());
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
        if (depth == 0 || board.gameOver()) {
            move.setValue(ponderHeuristicValue(move,player));
            return move;
        }
        int opponent = player == 1 ? 2 : 1;

        Set<Turn> children = generateMoves(player,move.getBoard());

        Turn bestMove = new Turn(player,Constants.WORSTVALUE,board.duplicate());
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

        Set<Turn> moves = new HashSet<>();

        Index index = getFinalIndex(side-1);
        generateMoves(player, new Turn(player,board.duplicate()), index.getRow(), index.getCol(), moves, board);

        return moves;
    }

    private void generateMoves(int player, Turn move, int row, int col, Set<Turn> moves, Board board) {
        Board moveBoard = move.getBoard();
        if (moveBoard.verifyTurn(row,col,player)) {
            move.addLine(row,col);
            if (moveBoard.turnContinues()) {
                continueMove(player,move,row,col,moves);
            } else {
                moves.add(move);
            }
        }

        if (col == 0) {
            if (row != 0) {
                Index index = getFinalIndex(row - 1);
                generateMoves(player, new Turn(player,board.duplicate()), index.getRow(), index.getCol(), moves, board);
            }
        } else {
            generateMoves(player, new Turn(player,board.duplicate()), row,col - 1, moves, board);
        }
    }

    private int[][] directions = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};

    private void continueMove(int player, Turn move, int row, int col, Set<Turn> moves) {
        System.out.println("continue");
        for (int[] direction : directions) {
            for (int i = 1; i < 3; i ++) {
                Turn newMove = move.duplicate();
                Board newBoard = newMove.getBoard();
                int newRow = row + i*direction[0];
                int newCol = col + i*direction[1];
                if (isValidIndex(newRow, newCol)) {
                    if (newBoard.verifyTurn(newRow, newCol, player)) {
                        newMove.addLine(newRow, newCol);
                        if (newBoard.turnContinues()) {
                            continueMove(player, newMove, newRow, newCol, moves);
                            return;
                        } else {
                            moves.add(newMove);
                            return;
                        }
                    }
                }

            }
        }
        System.out.println("no consegui vecino de");
        System.out.println(move);
        System.out.println(move.getBoard());
        Index index = getFinalIndex(side-1);
        generateMoves(player,move,index.getRow(),index.getCol(),moves,move.getBoard());
    }

    private Index getFinalIndex(int row) {
        int col;
        if (row%2 == 0)
            col = Parameters.size - 2;
        else
            col = Parameters.size - 1;
        return new Index(row,col);
    }

    private boolean isValidIndex(int row, int col) {
        if (row < 0 || row > side - 1 || col < 0)
            return false;
        if ( row%2 == 0 && col > Parameters.size - 2)
            return false;
        return !( row%2 == 1 && col > Parameters.size - 1);
    }

    private int ponderHeuristicValue(Turn move, int player) {
        int opponent = player == 1 ? 2 : 1;
        Board board = move.getBoard();

        return board.getPlayerScore(player) - board.getPlayerScore(opponent);
    }
}