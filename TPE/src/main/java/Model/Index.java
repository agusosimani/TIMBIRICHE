package Model;

import java.util.Objects;

class Index {
    private int row;
    private int col;

    Index(int row, int col) {
        this.row = row;
        this.col = col;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Index)) return false;
        Index index = (Index) o;
        return row == index.row &&
                col == index.col;
    }

    @Override
    public int hashCode() {

        return Objects.hash(row, col);
    }
}
