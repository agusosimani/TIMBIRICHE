package Model;

import java.io.FileWriter;
import java.io.IOException;

public class DotBuilder {
    private FileWriter output;
    private int id;
    private int startingPlayer;

    public DotBuilder(int player, String file) {
        try {
            output = new FileWriter(file);
            output.write("digraph {\n");
        } catch (IOException e) {
            output = null;
        }

        id = 0;
        startingPlayer = player;
    }
}
