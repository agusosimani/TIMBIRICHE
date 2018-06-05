package Model;

import Service.Parameters;
import static Service.Constants.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[][] boxes;
    private static int playerTurn;
    private int[] scores;
    private List<Move> movesDone;

    public Board() {
        boxes = new int[Parameters.size-1][Parameters.size-1];
        playerTurn = 1;
        scores = new int[2];
        movesDone = new ArrayList<Move>();
    }

    public int getPlayerTurn() { return playerTurn; }

    private void nextPlayer() {
        if (playerTurn == 1) {
            playerTurn = 2;
        } else {
            playerTurn = 1;
        }
    }

    public Move getLastMove() { return movesDone.get(movesDone.size()-1); }

    public int getPlayer1score() { return scores[0]; }

    public int getPlayer2score() { return scores[1]; }

    void addLine(int row, int col, int player) {
        boolean continues;
        // Check boxes containing the line
        if (isEven(row)) { //horizontal line
            switch (isBorder(row/2)) {
                case ISSUPERIOR:
                    continues = updateBoxes(row,col,player,TOP);
                    movesDone.add(new Move(row,col,row,col,0,0,player,continues,false));
                    break;
                case ISINFERIOR:
                    continues = updateBoxes(row/2-1,col,player,BOTTOM);
                    movesDone.add(new Move(row,col,row/2-1,col,0,0,player,continues,false));
                    break;
                default: // is not border
                    continues = updateBoxes(row/2-1,col,player,BOTTOM);
                    boolean continuesaux = updateBoxes(row/2,col,player,TOP);
                    movesDone.add(new Move(row,col,row/2-1,col,row/2,col,player,continues,continuesaux));
                    continues = continues || continuesaux;
                    break;
            }
        } else { //vertical line
            switch (isBorder(col)) {
                case ISLEFT:
                    continues = updateBoxes(row/2,col,player,LEFT);
                    movesDone.add(new Move(row,col,row/2,col,0,0,player,continues,false));
                    break;
                case ISRIGHT:
                    continues = updateBoxes(row/2,col-1,player,RIGHT);
                    movesDone.add(new Move(row,col,row/2,col-1,0,0,player,continues,false));
                    break;
                default: // is not border
                    continues = updateBoxes(row/2,col,player,LEFT);
                    boolean continuesaux = updateBoxes(row/2,col-1,player,RIGHT);
                    movesDone.add(new Move(row,col,row/2,col,row/2,col-1,player,continues,continuesaux));
                    continues = continues || continuesaux;
                    break;
            }
        }

        if (!continues) {
            nextPlayer();
        }
    }

    private boolean isEven(int num) {
        return num%2 == 0;
    }

    private int isBorder(int num) {
        if (num == 0) {
            return 0;
        } else if (num == Parameters.size-1) {
            return 1;
        } else {
            return 2;
        }
    }

    private boolean updateBoxes(int row, int col, int player, int line) {
        boxes[row][col] += Math.pow(2,line);
        if (boxes[row][col] == FULL) {
            scores[player-1] += 1;
            return true;
        }
        return false;
    }

    boolean gameOver() {
        return ((scores[0] + scores[1]) == (Math.pow(Parameters.size - 1, 2)));
    }

    // Returns number of player that won. If they tied, returns 0
    int getWinner() {
        if (scores[0] > scores[1]) {
            return 1;
        } else if (scores[0] < scores[1]){
            return 2;
        }
        return 0;
    }
}
