package Model;

import Service.Parameters;
import static Service.Constants.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[][] boxes;
    private int playerTurn;
    private int[] scores;
    private List<Move> movesDone;
    private boolean turnContinues;

    public Board() {
        boxes = new int[Parameters.size-1][Parameters.size-1];
        playerTurn = 1;
        scores = new int[2];
        movesDone = new ArrayList<>();
        turnContinues = false;
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

    int getPlayerScore(int player) { return scores[player-1]; }

    Board duplicate() {
        Board clone = new Board();
        clone.boxes = new int[Parameters.size-1][Parameters.size-1];
        for (int i=0; i<boxes.length; i++) {
            System.arraycopy(boxes[i], 0, clone.boxes[i], 0, boxes.length);
        }
        clone.playerTurn = playerTurn;
        clone.scores = new int[2];
        clone.scores[0] = scores[0];
        clone.scores[1] = scores[1];
        clone.movesDone = new ArrayList<>(movesDone);

        return clone;
    }

    boolean verifyTurn(int row, int col, int player) {
        return addLine(row,col,player,false);
    }

    void addLine(int row, int col, int player) { // row and col are the indexes of the button in the view matrix
        if (!addLine(row,col,player,true)) {
            nextPlayer();
        }
    }

    // returns true if player's turn continues, false if not
    private boolean addLine(int row, int col, int player, boolean update) {
        Move move = new Move(player);
        boolean continues;
        // Check boxes containing the line
        if (isEven(row)) { //horizontal line
            switch (isBorder(row/2)) {
                case ISSUPERIOR:
                    if (update) {
                        continues = update(row/2,col,TOP,player,move);
                        movesDone.add(move);
                    } else {
                        return isValid(row/2,col,TOP,player);
                    }
                    break;

                case ISINFERIOR:
                    if (update) {
                        continues = update(row/2-1,col,BOTTOM,player,move);
                        movesDone.add(move);
                    } else {
                        return isValid(row/2-1,col,BOTTOM,player);
                    }
                    break;

                default: // is not border
                    if (update) {
                        continues = update(row/2-1,col,BOTTOM,player,move);
                        continues |= update(row/2,col,TOP,player,move);
                        movesDone.add(move);
                    } else {
                        return isValid(row/2-1,col,BOTTOM,player) && isValid(row/2,col,TOP,player);
                    }
                    break;
            }
        } else { //vertical line
            switch (isBorder(col)) {
                case ISLEFT:
                    if (update) {
                        continues = update(row/2,col,LEFT,player,move);
                        movesDone.add(move);
                    } else {
                        return isValid(row/2,col,LEFT,player);
                    }
                    break;

                case ISRIGHT:
                    if (update) {
                        continues = update(row/2,col-1,RIGHT,player,move);
                        movesDone.add(move);
                    } else {
                        return isValid(row/2,col-1,RIGHT,player);
                    }
                    break;

                default: // is not border
                    if (update) {
                        continues = update(row/2,col,LEFT,player,move);
                        continues |= update(row/2,col-1,RIGHT,player,move);
                        movesDone.add(move);
                    } else {
                        return isValid(row/2,col,LEFT,player) && isValid(row/2,col-1,RIGHT,player);
                    }
                    break;
            }
        }
        return continues;
    }

    private boolean update(int row, int col, int type, int player, Move move) {
        Line line = new Line(row,col,type);
        updateBoxes(line,player);
        move.addLine(line);
        return line.tookBox();
    }

    private boolean isValid(int row, int col, int type, int player) {
        int valid = boxes[row][col] >> type;
        if (valid%2 == 0) {
            Line line = new Line(row,col,type);
            updateBoxes(line,player);
            if (line.tookBox())
                turnContinues = true;
            return true;
        }
        return false;
    }

    boolean turnContinues() {
        boolean ret = turnContinues;
        turnContinues = false;
        return ret;
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

        List<Line> lines = lastMove.getLines();
        for (Line line : lines) {
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

    @Override
    public String toString() {
        String s = "";
        for (int i=0; i<Parameters.size-1; i++) {
            for (int j=0; j<Parameters.size-1; j++) {
                s = s.concat(boxes[i][j]+" ");
            }
            s = s.concat("");
        }
        return s;
    }
}
