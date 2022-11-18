package schach2022;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessGame implements MouseListener {
    private final List<List<PositionFigureWrapper>> initData;
    private final JFrame frame;
    private final ChessFieldButton[][] grid;
    private long turn;

    private ChessFieldButton previousClickedFigureButton;
    private ChessFieldButton previousClickedEmptyButton;

    public ChessGame() {
        this.frame = new JFrame("My Chess");
        this.frame.setSize(850, 850);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setLocation(500, 100);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.turn = 0;

        this.grid = new ChessFieldButton[8][8];
        InitDataFigures.init();
        this.initData = InitDataFigures.get();
        this.initiateField();
    }

    @Override
    public String toString() {
        return Arrays.stream(this.grid).map(e -> Arrays.toString(e) + "\n").toList().stream().sequential().reduce(String::concat).orElseThrow();
    }

    private void initiateField() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                this.grid[i][j] = (new ChessFieldButton(new Position(i, j), ChessFigure.EMPTY, this));
            }
        }
        Arrays.stream(this.grid[1]).sequential().forEach(e -> e.setFigure(ChessFigure.PAWN_BLACK));
        Arrays.stream(this.grid[6]).sequential().forEach(e -> e.setFigure(ChessFigure.PAWN_WHITE));

        for (List<PositionFigureWrapper> initDatum : this.initData) {
            for (PositionFigureWrapper pFW : initDatum) {
                Position nextPos = pFW.position();

                this.grid[nextPos.getX()][nextPos.getY()] = new ChessFieldButton(nextPos, pFW.figureType(), this);
            }
        }

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                this.grid[i][j].setBounds(j * 100, i * 100, 100, 100);
                //this.grid.get(i).get(j).setPos(new Position(i, j));
                this.grid[i][j].setIcon();
                this.frame.add(this.grid[i][j]);
            }
        }
        this.frame.repaint();
    }
    public void moveIfWanted() {
        System.out.println("Hallo");
    }
    private void moveFigure(Position origin, Position dest) {
        this.turn++;
        ChessFieldButton originButton = this.grid[origin.getX()][origin.getY()];
        ChessFieldButton destButton = this.grid[dest.getX()][dest.getY()];
        ChessFigure toMove = this.grid[origin.getX()][origin.getY()].getFigureType();

        originButton.setFigure((toMove.color == Color.WHITE) ? ChessFigure.EMPTY_MOVED_WHITE : ChessFigure.EMPTY_MOVED_BLACK);
        destButton.setFigure(toMove);
        this.frame.repaint();
    }

    private boolean isEmpty(ChessFieldButton button) {
        return button.figureType == ChessFigure.EMPTY || button.figureType == ChessFigure.EMPTY_MOVED_BLACK || button.figureType == ChessFigure.EMPTY_MOVED_WHITE;
    }
    public List<Position> checkAvailablePos(ChessFieldButton buttonClicked) {
        List<Position> result = new ArrayList<>();

        if (buttonClicked.figureType == ChessFigure.EMPTY)
            return null;

        if (this.turn % 2 == 0) {
            switch (buttonClicked.figureType) {
                case ROOK_WHITE:
                    int buttonX = buttonClicked.getPos().getX();
                    int buttonY = buttonClicked.getPos().getY();

                    for (int i = buttonY + 1; i < this.grid.length; i++) {
                        if (!isEmpty(this.grid[buttonX][i])) {
                            break;
                        }
                        else
                            result.add(new Position(buttonX, i));
                    }
                    for (int i = buttonY - 1; i > 0; i--) {
                        if (!isEmpty(this.grid[buttonX][i])) {
                            break;
                        }
                        else
                            result.add(new Position(buttonX, i));
                    }

                    for (int i = buttonX + 1; i < this.grid.length; i++) {
                        if (!isEmpty(this.grid[i][buttonY])) {
                            break;
                        }
                        else
                            result.add(new Position(i, buttonY));
                    }
                    for (int i = buttonX - 1; i > 0; i--) {
                        if (!isEmpty(this.grid[i][buttonY])) {
                            break;
                        }
                        else
                            result.add(new Position(i, buttonY));
                    }

                    break;
                case KNIGHT_WHITE:

                    break;

                case BISHOP_WHITE:

                    break;
                case KING_WHITE:

                    break;

                case QUEEN_WHITE:

                    break;

                default:
                    throw new IllegalArgumentException("Tried to move wrong players figure!");
            }
        }

        else {
            switch (buttonClicked.figureType) {
                case ROOK_BLACK:
                    for (int i = buttonClicked.getX() + 1; i < this.grid.length; i++) {
                        if (this.grid[buttonClicked.getX()][i].figureType == ChessFigure.EMPTY) {
                            result.add(new Position(i, buttonClicked.getY()));
                        }
                    }
                    for (int i = buttonClicked.getY() + 1; i < this.grid[buttonClicked.getX()].length; i++) {
                        if (this.grid[buttonClicked.getX()][i].figureType == ChessFigure.EMPTY) {
                            result.add(new Position(i, buttonClicked.getX()));
                        }
                    }
                    break;
                case KNIGHT_BLACK:

                    break;

                case BISHOP_BLACK:

                    break;
                case KING_BLACK:

                    break;

                case QUEEN_BLACK:

                    break;

                default:
                    throw new IllegalArgumentException("Tried to move wrong players figure!");
            }
        }
        return result;
    }

    public static void main(String[] args) {


        ChessGame game = new ChessGame();
        //System.out.println(game);

        game.moveFigure(new Position(6,0), new Position(4, 0));
        game.moveIfWanted();

        game.turn++;
        List<Position> a = game.checkAvailablePos(game.grid[7][0]);
        //System.out.println(game.grid[0][7].figureType);
        System.out.println(a);
        //System.out.println(game);

        //System.out.println(game.getGrid());
        //game.printGrid();


        /*
        JFrame frame = new JFrame("Glossary");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton LookUpWord = new JButton("Look up word");  // create the button
        JPanel panel1 = new JPanel();  // create the panel
        panel1.add(LookUpWord);  // add the button to the panel
        frame.add(panel1, BorderLayout.NORTH);  // add the panel to the frame

        JButton SubmitNewWord = new JButton("Submit word");
        JPanel panel2 = new JPanel();
        panel2.add(SubmitNewWord);
        frame.add(panel2, BorderLayout.SOUTH);

        frame.setVisible(true);
        */
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ChessFieldButton buttonClicked = (ChessFieldButton) e.getSource();

        if (previousClickedFigureButton == null)
            previousClickedFigureButton = buttonClicked;
        if (previousClickedEmptyButton == null)
            previousClickedEmptyButton = buttonClicked;

        if (isEmpty(buttonClicked)) {
            if (buttonClicked.getBackground() == Color.DARK_GRAY) {
                buttonClicked.setBackground(Color.WHITE);
            }
            else {
                if (previousClickedEmptyButton.getBackground() != Color.BLUE && previousClickedEmptyButton.getBackground() != Color.GREEN)
                    previousClickedEmptyButton.setBackground(Color.WHITE);
                if (buttonClicked.getBackground() != Color.GREEN && previousClickedFigureButton.getBackground() == Color.BLUE)
                    buttonClicked.setBackground(Color.DARK_GRAY);
                previousClickedEmptyButton = buttonClicked;
            }
        }
        else {
            previousClickedEmptyButton.setBackground(Color.WHITE);
            if (buttonClicked.getBackground() == Color.blue) {
                buttonClicked.setBackground(Color.WHITE);
            }
            else {
                previousClickedFigureButton.setBackground(Color.WHITE);
                buttonClicked.setBackground(Color.blue);
                previousClickedFigureButton = buttonClicked;

            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}