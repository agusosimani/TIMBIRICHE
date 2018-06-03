package Model;

import java.util.Objects;

public class Move {
    private int row;
    private int col;
    private int player;

    public Move(int row, int col, int player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row &&
                col == move.col;
    }

    @Override
    public int hashCode() {

        return Objects.hash(row, col);
    }
}
