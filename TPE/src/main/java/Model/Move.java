package Model;

public class Move {
    //A move can modify the lines of two boxes
    private Line line1;
    private Line line2;
    private int player;

    Move(Line line1, Line line2, int player) {
        this.line1 = line1;
        this.line2 = line2;
        this.player = player;
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
