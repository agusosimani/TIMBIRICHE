package Model;

public class Move {
    private int buttonRow;
    private int buttonCol;
    //A move can modify the lines of two boxes
    private Line line1;
    private Line line2;
    private int player;

    Move(int buttonRow, int buttonCol, Line line1, Line line2, int player) {
        this.buttonRow = buttonRow;
        this.buttonCol = buttonCol;
        this.line1 = line1;
        this.line2 = line2;
        this.player = player;
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

    public Line getLine1() {
        return line1;
    }

    public Line getLine2() {
        return line2;
    }

    public int getPlayer() {
        return player;
    }
}
