package Controller;

import Model.*;
import Service.*;
import View.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static Service.Constants.*;

public class GameController {
    private static GameModel gameModel;
    private static BoardView boardView;
    private static DotBuilder dot;

    public static void main(String[] args) {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        try {
            if (argsList.size() != 10)
                throw new IllegalArgumentException();
            else {
                int size = argsList.indexOf("-size");

                if (size == -1) {
                    throw new IllegalArgumentException();
                }

                Parameters.size = Integer.parseInt(argsList.get(size+1));

                if (Parameters.size <= 0 || Parameters.size > 18) {
                    throw new IllegalArgumentException();
                }

                int ai = argsList.indexOf("-ai");

                if (ai == -1) {
                    throw new IllegalArgumentException();
                }

                Parameters.ai = Integer.parseInt(argsList.get(ai+1));

                if (Parameters.ai < 0 || Parameters.ai > 3) {
                    throw new IllegalArgumentException();
                }

                int mode = argsList.indexOf("-mode");

                if (mode == -1) {
                    throw new IllegalArgumentException();
                }

                Parameters.mode = argsList.get(mode+1);

                int param = argsList.indexOf("-param");

                if (param == -1) {
                    throw new IllegalArgumentException();
                }

                int paramValue = Integer.parseInt(argsList.get(param+1));

                switch (Parameters.mode) {
                    case "time":
                        Parameters.maxTime = paramValue;
                        if (Parameters.maxTime <= 0)
                            throw new IllegalArgumentException();
                        break;
                    case "depth":
                        Parameters.depth = paramValue;
                        if(Parameters.depth <= 0)
                            throw new IllegalArgumentException();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }

                int prune = argsList.indexOf("-prune");

                if (prune == -1) {
                    throw new IllegalArgumentException();
                }

                String pruneValue = argsList.get(prune+1);

                switch (pruneValue) {
                    case "on":
                        Parameters.prune = true;
                        break;
                    case "off":
                        Parameters.prune = false;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid parameters, try: \n" +
                    "\tjava -jar tpe.jar -size [n] -ai [m] -mode [time|depth] -param [k] -prune [on|off]\n\n" +
                    "\t-size [n] determines board size. 'n' should be an integer between 1 and 18\n" +
                    "\t-ai [m] determines AI rol, 'm' value can be:\n" +
                    "\t\t0: player vs player\n" +
                    "\t\t1: AI moves first\n" +
                    "\t\t2: AI moves second\n" +
                    "\t\t3: AI vs AI\n" +
                    "\t-mode [time|depth] determines if minimax algorithm ends due to time or depth\n" +
                    "\t-param [k] goes with previous parameter. If mode is 'time', 'k' determines seconds, if it is 'depth', tree depth. 'k' should be greater than 0\n" +
                    "\t-prune [on|off] enables or disables prune");
            return;
        }

        Board board = new Board();
        gameModel = new GameModel(board);
        boardView = new BoardView();
        dot = new DotBuilder();

        EventQueue.invokeLater(() -> {
            try {
                boardView.initFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        gameModel.gameLoop(dot);
    }

    public static void placeLine(int row, int col, int player) {
        gameModel.addLine(row,col,player);
        boardView.update(gameModel.getBoard());
    }

    public static ButtonIndex getIndex(Line line) {
        int row, col;
        switch (line.getType()) {
            case TOP:
                row = line.getBoxRow() * 2;
                col = line.getBoxCol();
                break;
            case BOTTOM:
                row = (line.getBoxRow() + 1) * 2;
                col = line.getBoxCol();
                break;
            case LEFT:
                row = line.getBoxRow() * 2 + 1;
                col = line.getBoxCol();
                break;
            default: //case RIGHT
                row = line.getBoxRow() * 2 + 1;
                col = line.getBoxCol() + 1;
                break;
        }

        return new ButtonIndex(row,col);
    }

    public static void setWinnerView(int player) {
        boardView.showWinner(player);
    }

    public static void performMove() {
        gameModel.performMove(true);
    }

    public static void undoMove() {
        Move move = gameModel.undoMove();
        if (move != null) {
            boardView.undoMove(gameModel.getBoard(),move);
        }
        gameModel.setMoveCalculated();
    }

    public static void generateDotFile() {
        dot.generateDotFile();
    }
}