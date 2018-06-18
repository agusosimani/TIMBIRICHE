package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {
    //A move can modify the lines of two boxes
    private List<Line> lines;
    private int player;


    Move(int player, Line ... lines) {
        this.player = player;
        this.lines = new ArrayList<>();
        Collections.addAll(this.lines, lines);
    }

    void addLine(Line line) {
        lines.add(line);
    }

    public List<Line> getLines() {
        return lines;
    }

    public int getPlayer() {
        return player;
    }
}
