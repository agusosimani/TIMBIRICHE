package Model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private int player;
    private Board board;
    private int value;
    private boolean pruned = false;
    private List<Index> lines;

    Turn(int player, Board board) {
        this.player = player;
        this.board = board;
        lines = new ArrayList<>();
    }

    Turn(int player, int value, Board board) {
        this.player = player;
        this.board = board;
        this.value = value;
        lines = new ArrayList<>();
    }

    Board getBoard() {
        return board;
    }

    int getValue() {
        return value;
    }

    public boolean isPruned() {
        return pruned;
    }

    List<Index> getLines() {
        return lines;
    }

    void setValue(int value) {
        this.value = value;
    }

    void setPruned() {
        this.pruned = true;
    }

    void addLine(int row, int col) {
        lines.add(new Index(row,col));
    }

    @Override
    public String toString() {
        String s = "turn:\n";
        for (Index index : lines) {
            s = s.concat("row: " + index.getRow() + " col: " + index.getCol() + "\n");
        }
        s = s.concat(" value: "+value);
        return s;
    }
}
