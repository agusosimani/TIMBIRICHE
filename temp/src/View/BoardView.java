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
    private JTextArea[][] boxesTaken;
    private boolean clickEnabled;

    private Color green = new Color(139,237,214);
    private Color grey = new Color(77,89,86);
    private Color blue = new Color(100,157,214);
    private Color pink = new Color(206,151,219);

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
        view.setMinimumSize(new Dimension(700,getWindowHeight()));
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
        title.setForeground(grey);
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
        boxesTaken = new JTextArea[size-1][size-1];
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
                    boxesTaken[row/2][col/2] = text;
                    board.add(text);
                } else {
                    JPanel buttonContainer = new JPanel();
                    buttonContainer.setBackground(Color.WHITE);
                    if (row%2==0 && col%2!=0) {
                        buttonContainer.setLayout(new GridLayout(3,1));
                    } else {
                        buttonContainer.setLayout(new GridLayout(1,3));
                    }
                    Button b = new Button(row,col/2);
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

        Button(int row, int col) {
            super();
            this.row = row;
            this.col = col;
        }

        int getRow() { return row; }

        int getCol() { return col; }
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
                    button.setBackground(pink);
                } else {
                    button.setBackground(blue);
                }
                button.removeMouseListener(mouseEvent);
                button.removeActionListener(this);
                if (playerTurn != ai && ai!=3) {
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
        undo.setBackground(grey);
        undo.setForeground(Color.WHITE);
        undo.setFont(Font.getFont("Tahoma"));
        undo.setMargin(new Insets(0,10,0,10));
        undo.addActionListener(new UndoListener());

        player1score = new JTextArea("Boxes player 1: 0");
        player1score.setEditable(false);
        player1score.setBackground(green);
        player1score.setMargin(new Insets(0,10,0,10));
        player1score.setForeground(pink);
        player1score.setFont(new Font("Tahoma",Font.PLAIN,16));
        player2score = new JTextArea("Boxes player 2: 0");
        player2score.setEditable(false);
        player2score.setBackground(green);
        player2score.setMargin(new Insets(0,10,0,10));
        player2score.setForeground(blue);
        player2score.setFont(new Font("Tahoma",Font.PLAIN,16));
        playerTurnLabel = new JTextArea("Turn: Player 1");
        playerTurnLabel.setEditable(false);
        playerTurnLabel.setBackground(green);
        playerTurnLabel.setMargin(new Insets(0,10,0,10));
        playerTurnLabel.setFont(new Font("Tahoma",Font.PLAIN,16));
        footer.add(player1score);
        footer.add(player2score);
        footer.add(playerTurnLabel);

        JButton dot = new JButton("Generate dot file");
        dot.setBackground(grey);
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
            Button button = buttons[move.getButtonRow()][move.getButtonCol()];
            button.doClick();
        }
        if (move.isTakenBox()) {
            if (move.getPlayer() == 1) {
                boxesTaken[move.getBoxRow()][move.getBoxCol()].setBackground(pink);
            } else {
                boxesTaken[move.getBoxRow()][move.getBoxCol()].setBackground(blue);
            }
        }
        if (move.isTakenSecondBox()) {
            if (move.getPlayer() == 1) {
                boxesTaken[move.getBox2Row()][move.getBox2Col()].setBackground(pink);
            } else {
                boxesTaken[move.getBox2Row()][move.getBox2Col()].setBackground(blue);
            }
        }
        playerTurn = board.getPlayerTurn();
        playerTurnLabel.setText("Turn: Player "+playerTurn);
        player1score.setText("Boxes player 1: "+board.getPlayer1score());
        player2score.setText("Boxes player 2: "+board.getPlayer2score());
    }

    public void showWinner(int player) {
        Dimension dim = new Dimension(700,80);
        JDialog winner = new JDialog();
        winner.setSize(dim);
        winner.setMaximumSize(dim);
        winner.setMinimumSize(dim);
        winner.setPreferredSize(dim);
        winner.setLayout(new GridBagLayout());

        JTextArea text = new JTextArea();
        if (player == 0) {
            text.setText("Both players won! Congratulations!");
        } else {
            text.setText("Player "+player+" won!\nCongratulations!");
        }
        text.setFont(new Font("Tahoma",Font.BOLD,20));
        text.setEditable(false);
        winner.add(text);
        winner.setVisible(true);
    }
}
