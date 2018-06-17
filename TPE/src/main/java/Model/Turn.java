package Model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private int player;
    private Board board;
    private int value;
    private boolean pruned = false;
    private List<Index> lines;

    public Turn(int player, Board board) {
        this.player = player;
        this.board = board;
        lines = new ArrayList<>();
    }

    public Turn(int player, int value, Board board) {
        this.player = player;
        this.board = board;
        this.value = value;
        lines = new ArrayList<>();
    }

    public int getPlayer() {
        return player;
    }

    public Board getBoard() {
        return board;
    }

    public int getValue() {
        return value;
    }

    public boolean isPruned() {
        return pruned;
    }

    public List<Index> getLines() {
        return lines;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setPruned(boolean pruned) {
        this.pruned = pruned;
    }

    public void addLine(int row, int col) {
        lines.add(new Index(row,col));
    }

    @Override
    public String toString() {
        String s = new String("turn:\n");
        for (Index index : lines) {
            s = s.concat("row: " + index.getRow() + " col: " + index.getCol() + "\n");
        }
        return s;
    }
}
