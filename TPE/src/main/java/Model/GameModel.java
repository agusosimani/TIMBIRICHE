package Model;

import Controller.GameController;
import Service.Parameters;
import static Service.Constants.*;

public class GameModel {
    private Board board;
    private boolean performMove;

    public GameModel(Board board) {
        this.board = board;
        this.performMove = false;
    }

    public Board getBoard() { return board; }

    public void gameLoop() {

        AI ai;
        if (Parameters.ai != PVSP) {
            ai = new AI(Parameters.ai); //Let him know if he goes first or second
        }

        int row = 0, col = 0;
        while(!board.gameOver()) {
            try {
                Thread.sleep(10);
            }
            catch (Exception e){
                continue;
            }

            if (board.getPlayerTurn() == Parameters.ai || Parameters.ai == 3) {
                if (performMove) {
                    GameController.placeLine(row++,col++,board.getPlayerTurn());
                }
                performMove = false;
            }
        }
        int winner = board.getWinner();
        GameController.setWinnerView(winner);
    }

    public void addLine(int row, int col, int player) {
        board.addLine(row,col,player);
    }

    public void performMove(boolean value) {
        this.performMove = value;
    }

    public Move undoMove() {
        return board.undoMove();
    }
}
