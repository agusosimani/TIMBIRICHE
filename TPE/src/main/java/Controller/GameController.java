package Controller;

import Model.*;
import Service.*;
import View.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameController {
    private static GameModel gameModel;
    private static BoardView boardView;

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

                if (Parameters.size <= 0) {
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
                        break;
                    case "depth":
                        Parameters.depth = paramValue;
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
                    "\tjava -jar tpe.jar -size [n] -ai [m] -mode [time|depth] -param [k] -prune [on|off]\n");
            return;
        }

        Board board = new Board();
        boardView = new BoardView();
        gameModel = new GameModel(board);

        EventQueue.invokeLater(() -> {
            try {
                boardView.initFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        gameModel.gameLoop();
    }

    public static void placeLine(int row, int col, int player) {
        gameModel.addLine(row,col,player);
        boardView.update(gameModel.getBoard());
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
    }
}