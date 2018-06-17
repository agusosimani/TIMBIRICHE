package Model;

import Service.Constants;
import Service.Parameters;

import java.util.ArrayList;
import java.util.List;

public class AI {
    private int player;
    private int opponent;
    private Board board;
    //private DotBuilder dot;

    AI(int player, Board board) {
        this.player = player;
        this.opponent = player == 1 ? 2 : 1;
        this.board = board;
    }

    public PossibleMove getMove() {
        PossibleMove current = new PossibleMove(opponent,board.duplicate());
        PossibleMove bestMove;
//        File tree1 = new File("tree.dot");
//        File tree2 = new File("tree2.dot");
//        dot = new DotBuilder(player,"tree2.dot");

        if(Parameters.maxTime != -1) {
            final long maxTime = Parameters.maxTime * 1000 + System.currentTimeMillis();
            TimeLimit timeLimit = () -> System.currentTimeMillis() > maxTime;

            int depth = 0;
            PossibleMove move = null;

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

    private PossibleMove getMove(PossibleMove current, int depth, TimeLimit timeLimit) {
        if (Parameters.prune)
            return negamax(current,depth,Constants.WORSTVALUE,Constants.BESTVALUE,player,timeLimit);
        else
            return negamaxNoPrune(current,depth,player,timeLimit);
    }

    private PossibleMove negamax(PossibleMove move, int depth, int alpha, int beta, int player, TimeLimit timeLimit) {
        if (depth == 0 || board.gameOver()) {
            move.setValue(ponderHeuristicValue(move,player));
            return move;
        }

        List<PossibleMove> children = generateMoves(player);

        PossibleMove bestMove = new PossibleMove(player,Constants.WORSTVALUE,board);
        for (PossibleMove child : children) {
            if (timeLimit.exceeded())
                break;

            if (beta > alpha) {
                child.setValue(-negamax(child,depth-1,-beta,-alpha,opponent,timeLimit).getValue());
                if (child.getValue() > bestMove.getValue())
                    bestMove = child;
                if (child.getValue() > alpha)
                    alpha = child.getValue();
            } else
                child.setPruned(true);
        }

        return bestMove;
    }

    private PossibleMove negamaxNoPrune(PossibleMove move, int depth, int player, TimeLimit timeLimit) {
        if (depth == 0 || board.gameOver()) {
            move.setValue(ponderHeuristicValue(move,player));
            return move;
        }

        List<PossibleMove> children = generateMoves(player);

        PossibleMove bestMove = new PossibleMove(Constants.WORSTVALUE);
        for (PossibleMove child : children) {
            if (timeLimit.exceeded())
                break;

            child.setValue(-negamaxNoPrune(child,depth-1,opponent,timeLimit).getValue());
            if (child.getValue() > bestMove.getValue())
                bestMove = child;
        }

        return bestMove;
    }

    private List<PossibleMove> generateMoves(int player) {
        List<PossibleMove> moves = new ArrayList<>();

        PossibleMove move = new PossibleMove(player,board.duplicate());


        for(int row = 0; row < Parameters.size - 1; row ++) {
            for (int col = 0; col < Parameters.size -1; col ++) {
                if (col == 0)
                    addLine(row,col,Constants.LEFT,moves);
                addLine(row,col,Constants.TOP,moves);
                addLine(row,col,Constants.RIGHT,moves);
                if (row == Parameters.size -2)
                    addLine(row,col,Constants.BOTTOM,moves);
            }
        }

        return moves;
    }

    private void addLine(int row, int col, int type, List<PossibleMove> moves) {
        Line line = new Line(row,col,Constants.TOP);
        if (board.verifyLine(line)) {
            Move move = new Move(player,line);
            moves.add(move);
            if (board.continues()) {
                generateNext(move);
            }
        }
    }

    private int ponderHeuristicValue(PossibleMove move, int player) {
        int opponent = player == 1 ? 2 : 1;
        Board board = move.getBoard();

        return board.getPlayerScore(player) - board.getPlayerScore(opponent);
    }
}