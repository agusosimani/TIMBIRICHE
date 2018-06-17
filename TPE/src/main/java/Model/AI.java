package Model;

import Service.Constants;
import Service.Parameters;

import java.util.ArrayList;
import java.util.List;

class AI {
    private int player;
    private int opponent;
    private Board board;
    private int side;
    //private DotBuilder dot;

    AI(Board board) {
        this.board = board;
        this.side = Parameters.size*2 - 1;
    }

    void setPlayer(int player) {
        this.player = player;
        this.opponent = player == 1 ? 2 : 1;
    }

    Turn getMove() {
        Turn current = new Turn(opponent, Constants.WORSTVALUE, board.duplicate());
        Turn bestMove;
//        File tree1 = new File("tree.dot");
//        File tree2 = new File("tree2.dot");
//        dot = new DotBuilder(player,"tree2.dot");

        if(Parameters.maxTime != -1) {
            final long maxTime = Parameters.maxTime * 1000 + System.currentTimeMillis();
            TimeLimit timeLimit = () -> System.currentTimeMillis() > maxTime;

            int depth = 0;
            Turn move = null;

            do {
                bestMove = move;
                depth++;
                move = getMove(current,depth,timeLimit);
            } while (!timeLimit.exceeded());

        } else {

            TimeLimit timeLimit = () -> false;
            bestMove = getMove(current,Parameters.depth,timeLimit);
        }

        return bestMove;
    }

    private Turn getMove(Turn current, int depth, TimeLimit timeLimit) {
        if (Parameters.prune)
            return negamax(current,depth,Constants.WORSTVALUE,Constants.BESTVALUE,player,timeLimit);
        else
            return negamaxNoPrune(current,depth,player,timeLimit);
    }

    private Turn negamax(Turn move, int depth, int alpha, int beta, int player, TimeLimit timeLimit) {
        if (depth == 0 || board.gameOver()) {
            move.setValue(ponderHeuristicValue(move,player));
            return move;
        }

        List<Turn> children = generateMoves(player,move.getBoard());

        Turn bestMove = new Turn(player,Constants.WORSTVALUE,board.duplicate());
        for (Turn child : children) {
            if (timeLimit.exceeded())
                break;

            if (beta > alpha) {
                child.setValue(-negamax(child,depth-1,-beta,-alpha,opponent,timeLimit).getValue());
                if (child.getValue() > bestMove.getValue()) {
                    bestMove = child;
                    System.out.println("actualizo");
                }
                if (child.getValue() > alpha)
                    alpha = child.getValue();
            } else
                child.setPruned();
        }
        move.setValue(bestMove.getValue());
        return bestMove;
    }

    private Turn negamaxNoPrune(Turn move, int depth, int player, TimeLimit timeLimit) {
        if (depth == 0 || board.gameOver()) {
            move.setValue(ponderHeuristicValue(move,player));
            return move;
        }

        List<Turn> children = generateMoves(player,move.getBoard());

        Turn bestMove = new Turn(player,Constants.WORSTVALUE,board.duplicate());
        for (Turn child : children) {
            if (timeLimit.exceeded())
                break;

            child.setValue(-negamaxNoPrune(child,depth-1,opponent,timeLimit).getValue());
            if (child.getValue() > bestMove.getValue())
                bestMove = child;
        }
        move.setValue(bestMove.getValue());
        return bestMove;
    }

    private List<Turn> generateMoves(int player, Board board) {
        List<Turn> moves = new ArrayList<>();

        int col;
        if ((side-1)%2 == 0)
            col = Parameters.size - 2;
        else
            col = Parameters.size - 1;
        generateMoves(player, new Turn(player,board.duplicate()), side-1, col,moves,board);

        return moves;
    }

    private void generateMoves(int player, Turn move, int row, int col, List<Turn> moves, Board board) {
        Board moveBoard = move.getBoard();
        if (moveBoard.verifyTurn(row,col,player)) {
            move.addLine(row,col);
            if (moveBoard.turnContinues()) {
                int newCol;
                if ((side-1)%2 == 0)
                    newCol = Parameters.size - 2;
                else
                    newCol = Parameters.size - 1;
                generateMoves(player,move,side-1,newCol,moves,board);
            }
            moves.add(move);
        }
        if (col == 0) {
            if (row != 0) {
                int newRow = row - 1;
                int newCol;
                if (newRow % 2 == 0)
                    newCol = Parameters.size - 2;
                else
                    newCol = Parameters.size - 1;
                generateMoves(player, new Turn(player, board.duplicate()), newRow, newCol, moves, board);
            }
        } else {
            generateMoves(player,new Turn(player,board.duplicate()),row,col - 1,moves, board);
        }
    }

    private int ponderHeuristicValue(Turn move, int player) {
        int opponent = player == 1 ? 2 : 1;
        Board board = move.getBoard();

        return board.getPlayerScore(player) - board.getPlayerScore(opponent);
    }
}