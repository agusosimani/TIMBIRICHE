package Model;

import Service.Parameters;

import java.io.File;
import java.util.*;

public class AI {
    private int player;
    private boolean scoutLayer;
    private DotBuilder dot;

    public AI(int player) {
        this.player = player;
    }

    public Move getMove(Board board) {
        int oponent = player == 1 ? 2 : 1;
        Move current;
        Move bestMove;
        File tree1 = new File("tree.dot");
        File tree2 = new File("tree2.dot");
        dot = new DotBuilder(player,"tree2.dot");

        if(Parameters.maxTime != -1) {
            final long maxTime = Parameters.maxTime * 1000 + System.currentTimeMillis();
            TimeLimit timeLimit = new TimeLimit() {
                @Override
                public boolean exceeded() {
                    return System.currentTimeMillis() > maxTime;
                }
            };

            int depth = 0;

            do {

            } while (!timeLimit.exceeded());


        }
        return new Move(null,null,0);
    }

}