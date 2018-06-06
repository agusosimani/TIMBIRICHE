package Model;

public class Line {
    private int boxRow;
    private int boxCol;
    private int type;
    private boolean tookBox;

    Line(int boxRow, int boxCol, int type) {
        this.boxRow = boxRow;
        this.boxCol = boxCol;
        this.type = type;
        tookBox = false;
    }

    public int getBoxRow() {
        return boxRow;
    }

    public int getBoxCol() {
        return boxCol;
    }

    int getType() {
        return type;
    }

    public boolean tookBox() {
        return tookBox;
    }

    void setTookBox() {
        this.tookBox = true;
    }
}
