package View;

import Controller.Controller;
import Model.Board;
import Model.Move;

import static Service.Constants.*;
import static Service.Parameters.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardView {
    private JFrame view;
    private JPanel header;
    private JPanel boardPanel;
    private JPanel footer;
    private JTextArea player1score;
    private JTextArea player2score;
    private JTextArea playerTurnLabel;
    private int playerTurn;
    private Button[][] buttons; //instances of buttons should be saved to remove color in case the undo button is pressed
    private boolean clickEnabled;

    Color green = new Color(66,244,113);

    public BoardView(){
        view = new JFrame();

        generateHeader();
        generateBoard();
        generateFooter();

        view.add(header, BorderLayout.NORTH);
        view.add(boardPanel, BorderLayout.CENTER);
        view.add(footer, BorderLayout.SOUTH);
        view.pack();

        view.setSize(getWindowWidth(),getWindowHeight());
        view.setMinimumSize(new Dimension(650,getWindowHeight()));
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void generateHeader() {
        header = new JPanel();

        Dimension dim = new Dimension(getWindowWidth(),50);
        header.setMinimumSize(dim);
        header.setMaximumSize(dim);
        header.setPreferredSize(dim);
        header.setBackground(green);
        header.setLayout(new GridBagLayout());

        JLabel title = new JLabel("TIMBIRICHE");
        title.setFont(new Font("Tahoma",Font.BOLD,25));
        title.setForeground(Color.BLACK);
        title.setBackground(green);

        header.add(title);
    }

    private void generateBoard(){
        boardPanel = new JPanel();

        Dimension boardPanelDim = new Dimension(getBoardDimension()+100,getBoardDimension());
        boardPanel.setMaximumSize(boardPanelDim);
        boardPanel.setMinimumSize(boardPanelDim);
        boardPanel.setPreferredSize(boardPanelDim);
        boardPanel.setBackground(Color.white);
        boardPanel.setLayout(new GridBagLayout());

        JPanel board = generateButtons();

        boardPanel.add(board);
    }

    private int getBoardSide(){
        return size*2-1;
    }

    private int getBoardDimension() {
        return size * SMALL + (size - 1) * LARGE;
    }

    private JPanel generateButtons() {
        JPanel board = new JPanel();

        int side = getBoardSide();
        board.setLayout(new GridLayout(side,side));
        int boardDim = getBoardDimension();
        Dimension dim = new Dimension(boardDim,boardDim);
        board.setMinimumSize(dim);
        board.setMaximumSize(dim);
        board.setPreferredSize(dim);

        buttons = new Button[side][size];
        clickEnabled = true;

        for (int row = 0; row < side; row ++) {
            for (int col = 0; col < side; col ++) {
                if (row%2==0 && col%2==0) {
                    Label dot = new Label("O");
                    int fontSize = boardDim / side;
                    dot.setFont(new Font("Serif",Font.BOLD,fontSize));
                    dot.setBackground(Color.WHITE);
                    dot.setForeground(green);
                    board.add(dot);
                } else if (row%2!=0 && col%2!=0) {
                    JTextArea text = new JTextArea("");
                    text.setEditable(false);
                    board.add(text);
                } else {
                    JPanel buttonContainer = new JPanel();
                    buttonContainer.setBackground(Color.WHITE);
                    boolean isVertical;
                    if (row%2==0 && col%2!=0) {
                        isVertical = false;
                        buttonContainer.setLayout(new GridLayout(3,1));
                    } else {
                        isVertical = true;
                        buttonContainer.setLayout(new GridLayout(1,3));
                    }
                    Button b = new Button(row,col/2,isVertical);
                    Hover h = new Hover();
                    b.addMouseListener(h);
                    ButtonListener bl = new ButtonListener(h);
                    b.addActionListener(bl);
                    b.setBorderPainted(false);
                    b.setBackground(Color.WHITE);

                    buttonContainer.add(new Label());
                    buttonContainer.add(b);
                    buttonContainer.add(new Label());

                    buttons[row][col/2] = b;
                    board.add(buttonContainer);
                }
            }
        }
        return board;
    }

    private class Button extends JButton {
        int row, col;
        boolean isVertical;

        public Button(int row, int col, boolean isVertical) {
            super();
            this.row = row;
            this.col = col;
            this.isVertical = isVertical;
        }

        public int getRow() { return row; }

        public int getCol() { return col; }

        public boolean isVertical() { return isVertical; }
    }

    private class ButtonListener implements ActionListener{
        Hover mouseEvent;

        private ButtonListener(Hover mouseEvent) {
            this.mouseEvent = mouseEvent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (clickEnabled) {
                clickEnabled = false;
                Button button = (Button) e.getSource();
                if (playerTurn == 1) {
                    button.setBackground(new Color(153,110,67));
                } else {
                    button.setBackground(new Color(100,157,214));
                }
                button.removeMouseListener(mouseEvent);
                button.removeActionListener(this);
                if (playerTurn != ai) {
                    Controller.placeLine(button.getRow(),button.getCol(), playerTurn);
                }
                clickEnabled = true;
            }

        }
    }

    private class Hover extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            Button button = (Button) e.getComponent();
            button.setBackground(new Color(240,240,240));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Button button = (Button) e.getComponent();
            button.setBackground(Color.WHITE);
        }
    }

    private void generateFooter() {
        footer = new JPanel();

        Dimension dim = new Dimension(getWindowWidth(),50);
        footer.setMinimumSize(dim);
        footer.setMaximumSize(dim);
        footer.setPreferredSize(dim);
        footer.setBackground(green);
        footer.setLayout(new GridBagLayout());

        JButton undo = new JButton("Undo move");
        undo.setBackground(Color.BLACK);
        undo.setForeground(Color.WHITE);
        undo.setFont(Font.getFont("Tahoma"));
        undo.setMargin(new Insets(0,10,0,10));
        undo.addActionListener(new UndoListener());

        player1score = new JTextArea("Boxes player 1: 0");
        player1score.setEditable(false);
        player1score.setBackground(green);
        player1score.setMargin(new Insets(0,10,0,10));
        player2score = new JTextArea("Boxes player 2: 0");
        player2score.setEditable(false);
        player2score.setBackground(green);
        player2score.setMargin(new Insets(0,10,0,10));
        playerTurnLabel = new JTextArea("Turn: Player 1");
        playerTurnLabel.setEditable(false);
        playerTurnLabel.setBackground(green);
        playerTurnLabel.setMargin(new Insets(0,10,0,10));
        footer.add(player1score);
        footer.add(player2score);
        footer.add(playerTurnLabel);

        JButton dot = new JButton("Generate dot file");
        dot.setBackground(Color.BLACK);
        dot.setForeground(Color.WHITE);
        dot.setFont(Font.getFont("Tahoma"));
        dot.setMargin(new Insets(0,10,0,10));
        dot.addActionListener(new DotListener());

        footer.add(undo);
        footer.add(dot);
    }

    private class UndoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class DotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }

    private int getWindowWidth() {
        return getBoardDimension() + 100;
    }

    private int getWindowHeight() {
        return getBoardDimension() + 100 + 100;
    }

    public void initFrame() {
        playerTurn = 1;
        view.setVisible(true);
    }

    public void update(Board board) {
        Move move = board.getLastMove();
        if (move.getPlayer() == ai) {
            Button button = buttons[move.getRow()][move.getCol()];
            button.doClick();
        }
        playerTurn = board.getPlayerTurn();
        playerTurnLabel.setText("Player "+playerTurn);
        player1score.setText("Boxes player 1: "+board.getPlayer1score());
        player2score.setText("Boxes player 2: "+board.getPlayer2score());
    }
}
