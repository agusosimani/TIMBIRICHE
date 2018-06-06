package Model;

public class Move {
    private int buttonRow;
    private int buttonCol;
    private int boxRow;
    private int boxCol;
    private int box2Row;
    private int box2Col;
    private int player;
    private boolean takenBox;
    private boolean takenSecondBox;

    Move(int buttonRow, int buttonCol, int boxRow, int boxCol, int box2Row, int box2Col, int player, boolean takenBox, boolean takenSecondBox) {
        this.buttonRow = buttonRow;
        this.buttonCol = buttonCol;
        this.boxRow = boxRow;
        this.boxCol = boxCol;
        this.box2Row = box2Row;
        this.box2Col = box2Col;
        this.player = player;
        this.takenBox = takenBox;
        this.takenSecondBox = takenSecondBox;
    }

    Move(int buttonRow, int buttonCol, int player) {
        this.buttonRow = buttonRow;
        this.buttonCol = buttonCol;
        this.player = player;
    }

    public int getButtonRow() {
        return buttonRow;
    }

    public int getButtonCol() {
        return buttonCol;
    }

    public int getBoxRow() {
        return boxRow;
    }

    public int getBoxCol() {
        return boxCol;
    }

    public int getBox2Row() {
        return box2Row;
    }

    public int getBox2Col() {
        return box2Col;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isTakenBox() {
        return takenBox;
    }

    public boolean isTakenSecondBox() {
        return takenSecondBox;
    }
}
