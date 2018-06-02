package Model;

import Service.Parameters;
import static Service.Constants.*;

public class Board {
    private int[][] boxes = new int[Parameters.size][Parameters.size];
    private int playerTurn = FIRST;
    private int player1score = 0;
    private int player2score = 0;
    private Move[] movesDone = null;

    public int getPlayerTurn() { return playerTurn; }

    public void nextPlayer() {
        playerTurn = -playerTurn;
    }

}
