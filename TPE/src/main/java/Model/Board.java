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
        movesDone = new ArrayList<>();
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

    void addLine(int row, int col, int player) { // row and col are the indexes of the button in the view matrix
        Line line1, line2;
        boolean continues;
        // Check boxes containing the line
        if (isEven(row)) { //horizontal line
            switch (isBorder(row/2)) {
                case ISSUPERIOR:
                    line1 = new Line(row,col,TOP);
                    updateBoxes(line1,player);
                    movesDone.add(new Move(line1,null,player));
                    continues = line1.tookBox();
                    break;

                case ISINFERIOR:
                    line1 = new Line(row/2-1,col,BOTTOM);
                    updateBoxes(line1,player);
                    movesDone.add(new Move(line1,null,player));
                    continues = line1.tookBox();
                    break;

                default: // is not border
                    line1 = new Line(row/2-1,col,BOTTOM);
                    updateBoxes(line1,player);
                    line2 = new Line(row/2,col,TOP);
                    updateBoxes(line2,player);
                    movesDone.add(new Move(line1,line2,player));
                    continues = line1.tookBox() || line2.tookBox();
                    break;
            }
        } else { //vertical line
            switch (isBorder(col)) {
                case ISLEFT:
                    line1 = new Line(row/2,col,LEFT);
                    updateBoxes(line1, player);
                    movesDone.add(new Move(line1,null, player));
                    continues = line1.tookBox();
                    break;

                case ISRIGHT:
                    line1 = new Line(row/2,col-1,RIGHT);
                    updateBoxes(line1,player);
                    movesDone.add(new Move(line1,null, player));
                    continues = line1.tookBox();
                    break;

                default: // is not border
                    line1 = new Line(row/2,col,LEFT);
                    updateBoxes(line1,player);
                    line2 = new Line(row/2,col-1,RIGHT);
                    updateBoxes(line2,player);
                    movesDone.add(new Move(line1,line2, player));
                    continues = line1.tookBox() || line2.tookBox();
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

    private void updateBoxes(Line line, int player) {
        boxes[line.getBoxRow()][line.getBoxCol()] += Math.pow(2,line.getType());
        if (boxes[line.getBoxRow()][line.getBoxCol()] == FULL) {
            scores[player-1] ++;
            line.setTookBox();
        }
    }

    Move undoMove() {
        if (movesDone.size() == 0) {
            return null;
        }
        Move lastMove = movesDone.remove(movesDone.size()-1);
        Line line = lastMove.getLine1();
        if (boxes[line.getBoxRow()][line.getBoxCol()] == FULL) {
            scores[lastMove.getPlayer()-1] --;
        }
        boxes[line.getBoxRow()][line.getBoxCol()] -= Math.pow(2,line.getType());
        line = lastMove.getLine2();
        if (line != null) {
            if (boxes[line.getBoxRow()][line.getBoxCol()] == FULL) {
                scores[lastMove.getPlayer()-1] --;
            }
            boxes[line.getBoxRow()][line.getBoxCol()] -= Math.pow(2,line.getType());
        }
        playerTurn = lastMove.getPlayer();
        return lastMove;
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
