package Model;

public class PossibleMove extends Move {
    private Board board;
    private int value;
    private boolean pruned;

    public PossibleMove(int player, int value, Board board) {
        super(player);
        this.board = board;
        this.value = value;
    }

    public PossibleMove(int player, Board board) {
        super(player);
        this.board = board;
    }

    public PossibleMove(int value) {
        super();
        this.value = value;
    }

    public Board getBoard() {
        return board;
    }

    public int getValue() {
        return value;
    }

    public boolean isPruned() {
        return pruned;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setPruned(boolean pruned) {
        this.pruned = pruned;
    }
}
