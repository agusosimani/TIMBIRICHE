package Model;

import Controller.Controller;
import Service.Parameters;
import static Service.Constants.*;

public class Model {
    private Board board;

    public Model(Board board) {
        this.board = board;
    }

    public Board getBoard() { return board; }

    public void gameLoop() {

        AI ai;
        if (Parameters.ai != PVSP) {
            if (Parameters.ai != AIVSAI) {
                ai = new AI(Parameters.ai); //Let him know if he goes first or second
            }
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
                Controller.placeLine(row++,col++,board.getPlayerTurn());
            }
        }
        int winner = board.getWinner();
        Controller.setWinnerView(winner);
    }

    public void addLine(int row, int col, int player) {
        board.addLine(row,col,player);
    }
}
