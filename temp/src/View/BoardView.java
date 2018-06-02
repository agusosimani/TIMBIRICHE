package View;

import Service.Constants;
import Service.Parameters;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class BoardView {
    private JFrame view;
    private JPanel header;
    private JPanel boardPanel;
    private JPanel footer;
    private JTextArea player1score;
    private JTextArea player2score;
    private JTextArea playerTurn;

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
        header.setBackground(Color.CYAN);
        header.setLayout(new GridBagLayout());

        JLabel title = new JLabel("TIMBIRICHE");
        title.setFont(Font.getFont("Tahoma"));
        title.setForeground(Color.BLACK);
        title.setBackground(Color.CYAN);

        header.add(title);
    }

    private void generateBoard(){
        boardPanel = new JPanel();

        Dimension boardPanelDim = new Dimension(getBoardDimension()+100,getBoardDimension()+100);
        boardPanel.setMaximumSize(boardPanelDim);
        boardPanel.setMinimumSize(boardPanelDim);
        boardPanel.setPreferredSize(boardPanelDim);
        boardPanel.setBackground(Color.white);
        boardPanel.setLayout(new GridBagLayout());

        JPanel board = generateButtons();

        boardPanel.add(board);
    }

    private int getBoardSide(){
        return Parameters.size*2-1;
    }

    private int getBoardDimension() {
        return Parameters.size * Constants.SMALL + (Parameters.size - 1) * Constants.LARGE;
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

        for (int row = 0; row < side; row ++) {
            for (int col = 0; col < side; col ++) {
                if (row%2==0 && col%2==0) {
                    Label dot = new Label("O");
                    dot.setFont(new Font("Serif",Font.BOLD,35));
                    dot.setBackground(Color.WHITE);
                    dot.setForeground(Color.CYAN);
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
                    Button b = new Button(row,col,isVertical);
                    ButtonListener bl = new ButtonListener();
                    b.addActionListener(bl);
                    b.setBorderPainted(false);
                    b.setBackground(new Color(240,240,240));

                    buttonContainer.add(new Label());
                    buttonContainer.add(b);
                    buttonContainer.add(new Label());

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
        public void actionPerformed(ActionEvent e) {
            Button button = (Button) e.getSource();
            button.setBackground(new Color(153,110,67));
            System.out.println("Row: "+button.getRow());
            System.out.println("Col: "+button.getCol());
        }

    }

    private void generateFooter() {
        footer = new JPanel();

        Dimension dim = new Dimension(getWindowWidth(),50);
        footer.setMinimumSize(dim);
        footer.setMaximumSize(dim);
        footer.setPreferredSize(dim);
        footer.setBackground(Color.CYAN);
        footer.setLayout(new GridBagLayout());

        JButton undo = new JButton("Undo move");
        undo.setBackground(Color.BLACK);
        undo.setForeground(Color.WHITE);
        undo.setFont(Font.getFont("Tahoma"));
        undo.setMargin(new Insets(0,10,0,10));
        undo.addActionListener(new UndoListener());

        player1score = new JTextArea("Boxes player 1: 0");
        player1score.setEditable(false);
        player1score.setBackground(Color.CYAN);
        player1score.setMargin(new Insets(0,10,0,10));
        player2score = new JTextArea("Boxes player 2: 0");
        player2score.setEditable(false);
        player2score.setBackground(Color.CYAN);
        player2score.setMargin(new Insets(0,10,0,10));
        playerTurn = new JTextArea("Turn: Player 1");
        playerTurn.setEditable(false);
        playerTurn.setBackground(Color.CYAN);
        playerTurn.setMargin(new Insets(0,10,0,10));
        footer.add(player1score);
        footer.add(player2score);
        footer.add(playerTurn);

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
        view.setVisible(true);
    }

    public void update() {

    }
}
