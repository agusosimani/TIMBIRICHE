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

    public void gameLoop(DotBuilder dot) {

        AI ai = new AI(dot);
        Turn turn = null;

        while(!board.gameOver()) {
            try {
                Thread.sleep(10);
            }
            catch (Exception e){
                continue;
            }

            if (board.getPlayerTurn() == Parameters.ai || Parameters.ai == AIVSAI) {
                if (!moveCalculated) {
                    ai.setPlayer(board.getPlayerTurn());
                    turn = ai.getMove(board);
                    moveCalculated = true;
                }
                if (performMove && moveCalculated) {
                    assert turn != null;
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
