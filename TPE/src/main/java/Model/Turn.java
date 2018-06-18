package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Turn {
    private Board board;
    private int value;
    private boolean pruned = false;
    private List<Index> lines;

    Turn(Board board) {
        this.board = board;
        lines = new ArrayList<>();
    }

    Turn(int value, Board board) {
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

    Turn duplicate() {
        Turn duplicated = new Turn(board.duplicate());
        duplicated.lines = new ArrayList<>(lines);
        return duplicated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turn)) return false;
        Turn turn = (Turn) o;
        return Objects.equals(lines, turn.lines);
    }

    @Override
    public int hashCode() {

        return Objects.hash(lines);
    }

    @Override
    public String toString() {
        String s = "turn: ";
        for (Index index : lines) {
            s = s.concat(" row: " + index.getRow() + " col: " + index.getCol());
        }
        s = s.concat(" value: "+value);
        return s;
    }
}
