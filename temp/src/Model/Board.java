package Model;

import Service.Parameters;
import static Service.Constants.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private int[][] boxes;
    private static int playerTurn;
    private int player1score;
    private int player2score;
    private List<Move> movesDone;

    public Board() {
        boxes = new int[Parameters.size][Parameters.size];
        playerTurn = 1;
        player1score = 0;
        player2score = 0;
        movesDone = new ArrayList<Move>();
    }

    public int getPlayerTurn() { return playerTurn; }

    public void nextPlayer() {
        if (playerTurn == 1) {
            playerTurn = 2;
        } else {
            playerTurn = 1;
        }
    }

    public Move getLastMove() { return movesDone.get(movesDone.size()-1); }

    public int getPlayer1score() { return player1score; }

    public int getPlayer2score() { return player2score; }

    public void addLine(int row, int col, int player) {
        movesDone.add(new Move(row,col,player));
        //modificar boxes
        //chequear si hay que sumar score
        //chequear si se gano
        nextPlayer();
    }

    public boolean gameOver() {
        return (player1score + player2score) == (Math.pow(Parameters.size,2));
    }
}
