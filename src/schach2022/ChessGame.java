package schach2022;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessGame {

    private final List<List<PositionFigureWrapper>> initData;
    private final JFrame frame;
    private final ChessFieldButton[][] grid;

    private long turn;


    public ChessGame() {
        this.frame = new JFrame("My Chess");
        this.frame.setSize(850, 850);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setLocation(400, 200);
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

    private static <E> Stream<E> flatStreamOf(List<List<E>> list) {
        return list.stream().flatMap(Collection::stream);
    }

    private void initiateField() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                this.grid[i][j] = (new ChessFieldButton(new Position(i, j), ChessFigure.EMPTY));
            }
        }
        //flatStreamOf(this.grid).forEach((e) -> e = ChessFigure.EMPTY);
        Arrays.stream(this.grid[1]).sequential().peek(e -> e.setFigure(ChessFigure.PAWN_BLACK)).collect(Collectors.toList());
        Arrays.stream(this.grid[6]).sequential().peek(e -> e.setFigure(ChessFigure.PAWN_WHITE)).collect(Collectors.toList());

        for (int i = 0; i < this.initData.size(); i++) {
            for (int j = 0; j < this.initData.get(i).size(); j++) {
                Position nextPos = this.initData.get(i).get(j).position();

                this.grid[nextPos.getX()][nextPos.getY()] = new ChessFieldButton(nextPos, this.initData.get(i).get(j).figureType());
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
        //Arrays.stream(this.frame.getComponents()).forEach(e -> e.setVisible(true));
        //this.frame.setPreferredSize(new Dimension(800, 800));
        this.frame.repaint();
    }
    public void moveIfWanted() {

    }
    private void moveFigure(Position origin, Position dest) {
        ChessFieldButton originButton = this.grid[origin.getX()][origin.getY()];
        ChessFieldButton destButton = this.grid[dest.getX()][dest.getY()];
        ChessFigure toMove = this.grid[origin.getX()][origin.getY()].getFigureType();

        originButton.setFigure((toMove.color == Color.WHITE) ? ChessFigure.EMPTY_MOVED_WHITE : ChessFigure.EMPTY_MOVED_BLACK);
        destButton.setFigure(toMove);
        this.frame.repaint();
    }
    public void printGrid() {
        for (int i = 0; i < 1; i++) {
            System.out.println(Arrays.toString(this.grid[i]));
        }
    }
    public List<Position> checkAvailablePos(ChessFieldButton buttonClicked) {
        List<Position> result = new ArrayList<>();

        if (buttonClicked.figureType == ChessFigure.EMPTY)
            return null;

        if (this.turn % 2 == 0) {
            switch (buttonClicked.figureType) {
                case ROOK_WHITE:
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
        game.moveFigure(new Position(1,0), new Position(3, 0));
        List<Position> a = game.checkAvailablePos(game.grid[7][0]);
        System.out.println(game.grid[0][7].figureType);
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
}