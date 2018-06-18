package Model;

import Controller.GameController;
import Service.Parameters;
import static Service.Constants.*;

public class GameModel {
    private Board board;
    private boolean performMove;
    private boolean moveCalculated;

    public GameModel(Board board) {
        this.board = board;
        this.performMove = false;
        this.moveCalculated = false;
    }

    public Board getBoard() { return board; }

    public void gameLoop() {

        AI ai = new AI(board);
        Turn turn = null;
        if (Parameters.ai != PVSP) {
            if (Parameters.ai != AIVSAI)
                ai.setPlayer(Parameters.ai); //Let him know if he goes first or second
            else
                ai.setPlayer(1);
        }

        while(!board.gameOver()) {
            try {
                Thread.sleep(10);
            }
            catch (Exception e){
                continue;
            }

            if (board.getPlayerTurn() == Parameters.ai || Parameters.ai == AIVSAI) {
                if (!moveCalculated) {
                    turn = ai.getMove();
                    //System.out.println("finished turn calculation");
                    //System.out.println(turn);
                    moveCalculated = true;
                }
                if (performMove && moveCalculated) {
                    for (Index index : turn.getLines())
                        GameController.placeLine(index.getRow(),index.getCol(),board.getPlayerTurn());
                    performMove = false;
                    moveCalculated = false;
                    ai.setPlayer(board.getPlayerTurn());
                }
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

    public void setMoveCalculated() {
        moveCalculated = false;
    }
}
