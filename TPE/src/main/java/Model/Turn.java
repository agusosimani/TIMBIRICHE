package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Turn {
    private Board board;
    private int player;
    private int id;
    private int value;
    private boolean first;
    private boolean pruned = false;
    private List<Index> lines;

    Turn(int player, Board board) {
        this.board = board;
        this.player = player;
        this.first = false;
        lines = new ArrayList<>();
    }

    Turn(int player, int value, Board board) {
        this.board = board;
        this.player = player;
        this.value = value;
        this.first = false;
        lines = new ArrayList<>();
    }

    Board getBoard() {
        return board;
    }

    int getPlayer() {
        return player;
    }

    int getId() {
        return id;
    }

    int getValue() {
        return value;
    }

    boolean isFirst() {
        return first;
    }

    boolean isPruned() {
        return pruned;
    }

    List<Index> getLines() {
        return lines;
    }

    void setId(int id) {
        this.id = id;
    }

    void setValue(int value) {
        this.value = value;
    }

    void setFirst() {
        this.first = true;
    }

    void setPruned() {
        this.pruned = true;
    }

    void addLine(int row, int col) {
        lines.add(new Index(row,col));
    }

    Turn duplicate() {
        Turn duplicated = new Turn(player, board.duplicate());
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
        StringBuilder s = new StringBuilder();
        for (Index index : lines) {
            s.append("(").append(index.getRow()).append(",").append(index.getCol()).append(") ");
        }
        return s.toString();
    }
}
