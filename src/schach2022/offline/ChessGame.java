package schach2022.offline;

import schach2022.gameUtils.Position;

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

    private ChessFieldButton movedFromMarkerButton;
    private ChessFieldButton movedToMarkerButton;
    private List<Position> markedPos;
    private ChessFieldButton previousClickedFigureButton;


    public ChessGame() {
        this.frame = new JFrame("My Chess");
        this.frame.setSize(850, 850);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setLocation(500, 100);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.turn = 0;
        this.markedPos = new ArrayList<>();
        this.movedFromMarkerButton = new ChessFieldButton(null, null, null);
        this.movedToMarkerButton = new ChessFieldButton(null, null, null);

        this.grid = new ChessFieldButton[8][8];
        InitDataFigures.init();
        this.initData = InitDataFigures.get();
        this.initiateField();

        this.frame.repaint();
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
        Arrays.stream(this.grid[1]).forEach(e -> e.setFigure(ChessFigure.PAWN_BLACK));
        Arrays.stream(this.grid[6]).forEach(e -> e.setFigure(ChessFigure.PAWN_WHITE));

        for (List<PositionFigureWrapper> initDatum : this.initData) {
            for (PositionFigureWrapper pFW : initDatum) {
                Position nextPos = pFW.position();
                this.grid[nextPos.getX()][nextPos.getY()].figureType = pFW.figureType();
                // = new ChessFieldButton(nextPos, pFW.figureType(), this);
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

    private void moveFigure(Position origin, Position dest) {
        this.movedFromMarkerButton.setMovedAway(false);
        this.movedFromMarkerButton.setBackground(Color.WHITE);
        this.movedToMarkerButton.setMovedTo(false);
        this.movedToMarkerButton.setBackground(Color.WHITE);
        this.turn++;
        ChessFieldButton originButton = this.grid[origin.getX()][origin.getY()];
        ChessFieldButton destButton = this.grid[dest.getX()][dest.getY()];
        ChessFigure toMove = this.grid[origin.getX()][origin.getY()].getFigureType();

        originButton.setFigure(ChessFigure.EMPTY);
        originButton.setBackground(Color.GREEN);
        originButton.setMovedAway(true);

        destButton.setFigure(toMove);
        destButton.setTouched(true);
        destButton.setBackground(Color.PINK);
        destButton.setMovedTo(true);

        this.movedFromMarkerButton = originButton;
        this.movedToMarkerButton = destButton;

        this.removeMarker();
        this.previousClickedFigureButton = null;

        for (ChessFieldButton[] ch : this.grid) {
            for (ChessFieldButton c : ch) {
                c.setBounds(c.getX(), 700 -c.getY(), 100, 100);
            }
        }

        this.frame.repaint();
    }



    private boolean isEmpty(ChessFieldButton button) {
        return button.figureType == ChessFigure.EMPTY;
    }

    private List<Position> getRookPositions(ChessFieldButton buttonClicked, int buttonX, int buttonY) {
        List<Position> result = new ArrayList<>();
        for (int i = buttonY + 1; i < this.grid.length; i++) {
            if (!isEmpty(this.grid[buttonX][i])) {
                if (buttonClicked.figureType.color != this.grid[buttonX][i].figureType.color)
                    result.add(new Position(buttonX, i));
                break;
            }
            else
                result.add(new Position(buttonX, i));
        }
        for (int i = buttonY - 1; i >= 0; i--) {
            if (!isEmpty(this.grid[buttonX][i])) {
                if (buttonClicked.figureType.color != this.grid[buttonX][i].figureType.color)
                    result.add(new Position(buttonX, i));
                break;
            }
            else
                result.add(new Position(buttonX, i));
        }

        for (int i = buttonX + 1; i < this.grid.length; i++) {
            if (!isEmpty(this.grid[i][buttonY])) {
                if (buttonClicked.figureType.color != this.grid[i][buttonY].figureType.color)
                    result.add(new Position(i, buttonY));
                break;
            }
            else
                result.add(new Position(i, buttonY));
        }
        for (int i = buttonX - 1; i >= 0; i--) {
            if (!isEmpty(this.grid[i][buttonY])) {
                if (buttonClicked.figureType.color != this.grid[i][buttonY].figureType.color)
                    result.add(new Position(i, buttonY));
                break;
            }
            else
                result.add(new Position(i, buttonY));
        }
        return result;
    }
    private List<Position> getBishopPositions(ChessFieldButton buttonClicked, int buttonX, int buttonY) {
        List<Position> result = new ArrayList<>();
        for (int i = buttonX + 1, j = buttonY +1; i < this.grid.length && j < this.grid.length; i++, j++) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }
        for (int i = buttonX - 1, j = buttonY -1; i >= 0 && j >= 0; i--, j--) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }

        for (int i = buttonX + 1, j = buttonY -1; i < this.grid.length && j >= 0; i++, j--) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }
        for (int i = buttonX - 1, j = buttonY +1; i >= 0 && j < this.grid.length; i--, j++) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }
        return result;
    }
    private List<Position> findAvailablePos(ChessFieldButton buttonClicked) {
        List<Position> result = new ArrayList<>();

        if (buttonClicked.figureType == ChessFigure.EMPTY)
            return null;

        int buttonX = buttonClicked.getPos().getX();
        int buttonY = buttonClicked.getPos().getY();

        switch (buttonClicked.figureType) {
            case ROOK_WHITE, ROOK_BLACK:

                result = getRookPositions(buttonClicked, buttonX, buttonY);

                break;

            case BISHOP_WHITE, BISHOP_BLACK:

                result = getBishopPositions(buttonClicked, buttonX, buttonY);

                break;

            case QUEEN_WHITE, QUEEN_BLACK:

                result.addAll(getRookPositions(buttonClicked, buttonX, buttonY));
                result.addAll(getBishopPositions(buttonClicked, buttonX, buttonY));

                break;

            case KNIGHT_WHITE, KNIGHT_BLACK:
                if (buttonX > 1 && buttonY < this.grid.length -1 && (isEmpty(this.grid[buttonX -2][buttonY +1])
                        || this.grid[buttonX -2][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -2, buttonY +1));

                if (buttonX > 1 && buttonY > 0 && (buttonY < this.grid.length && isEmpty(this.grid[buttonX -2][buttonY -1])
                        || this.grid[buttonX -2][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -2, buttonY -1));

                // Movement up -> left and right

                if (buttonX < this.grid.length -2 && buttonY < this.grid.length -1 && (isEmpty(this.grid[buttonX +2][buttonY +1])
                        || this.grid[buttonX +2][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +2, buttonY +1));

                if (buttonX < this.grid.length -2 && buttonY > 0 && (buttonY < this.grid.length && isEmpty(this.grid[buttonX +2][buttonY -1])
                        || this.grid[buttonX +2][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +2, buttonY -1));

                // Movement down -> left and right

                if (buttonX < this.grid.length -1 && buttonY < this.grid.length -2 && (isEmpty(this.grid[buttonX +1][buttonY +2])
                        || this.grid[buttonX +1][buttonY +2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY +2));

                if (buttonX > 0 && buttonY < this.grid.length -2 && (isEmpty(this.grid[buttonX -1][buttonY +2])
                        || this.grid[buttonX -1][buttonY +2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY +2));

                // Movement right -> up and down

                if (buttonX < this.grid.length -1 && buttonY > 1 && (isEmpty(this.grid[buttonX +1][buttonY -2])
                        || this.grid[buttonX +1][buttonY -2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY -2));

                if (buttonX > 0 && buttonY > 1 && (isEmpty(this.grid[buttonX -1][buttonY -2])
                        || this.grid[buttonX -1][buttonY -2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY -2));

                // Movement left -> up and down

                break;

            case KING_WHITE, KING_BLACK:
                if (buttonX < this.grid.length-1 && (isEmpty(this.grid[buttonX +1][buttonY])
                        || this.grid[buttonX +1][buttonY].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY));

                if (buttonX < this.grid.length-1 && (buttonY < this.grid.length && isEmpty(this.grid[buttonX +1][buttonY +1])
                        || this.grid[buttonX +1][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY +1));

                if (buttonX < this.grid.length-1 && (isEmpty(this.grid[buttonX][buttonY +1])
                        || this.grid[buttonX][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX, buttonY +1));

                if (buttonX > 0 && (isEmpty(this.grid[buttonX -1][buttonY])
                        || this.grid[buttonX -1][buttonY].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY));


                if (buttonX > 0 && buttonY > 0 && (isEmpty(this.grid[buttonX -1][buttonY -1])
                        || this.grid[buttonX -1][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY -1));

                if (buttonY > 0 && (isEmpty(this.grid[buttonX][buttonY -1])
                        || this.grid[buttonX][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX, buttonY -1));

                if (buttonX > 0 && buttonY < this.grid.length-1 && (isEmpty(this.grid[buttonX -1][buttonY +1])
                        || this.grid[buttonX -1][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY +1));

                if (buttonX < this.grid.length-1 && buttonY > 0 && (isEmpty(this.grid[buttonX +1][buttonY -1])
                        || this.grid[buttonX +1][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY -1));
                break;

            case PAWN_WHITE, PAWN_BLACK:
                if (buttonClicked.figureType == ChessFigure.PAWN_BLACK) {
                    for (int i = buttonX +1; i < this.grid.length; i++) {
                        if (result.size() > 1 || !isEmpty(this.grid[i][buttonY]))
                            break;
                        else {
                            if (result.size() == 1 && buttonClicked.isTouched())
                                break;
                            result.add(new Position(i, buttonY));
                        }
                    }
                }
                else {
                    for (int i = buttonX - 1; i > 0; i--) {
                        if (result.size() > 1 || !isEmpty(this.grid[i][buttonY]))
                            break;
                        else {
                            if (result.size() == 1 && buttonClicked.isTouched())
                                break;
                            result.add(new Position(i, buttonY));
                        }
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Clicked on Empty field!");
        }
        return result;
    }

    private void removeMarker() {
        for (Position p: this.markedPos) {
            if (this.grid[p.getX()][p.getY()].wasMovedTo()) {
                this.grid[p.getX()][p.getY()].setBackground(Color.PINK);
                continue;
            }
            else
                this.grid[p.getX()][p.getY()].setBackground(Color.WHITE);

            if (this.grid[p.getX()][p.getY()].wasMovedAway())
                this.grid[p.getX()][p.getY()].setBackground(Color.GREEN);
            else
                this.grid[p.getX()][p.getY()].setBackground(Color.WHITE);
        }
    }
    private void markAvailablePos(ChessFieldButton button) {
        List<Position> positions = this.findAvailablePos(button);

        this.removeMarker();

        if (positions == null)
            return;
        for (Position p : positions) {
            this.grid[p.getX()][p.getY()].setBackground(Color.GRAY);
        }
        this.markedPos = new ArrayList<>(positions);
        this.frame.repaint();
    }

    /**
     * Is called, if a playbutton (figure or empty field) is clicked. Launches {@code moveFigure()} if a figure and a valid empty field is selected in order.
     * sets colorscheme for markings aswell.
     * @param e the click-event to be processed.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        ChessFieldButton buttonClicked = (ChessFieldButton) e.getSource();

        if (previousClickedFigureButton == null)
            previousClickedFigureButton = buttonClicked;

        if (buttonClicked.getBackground() == Color.blue) {
            buttonClicked.setBackground(Color.WHITE);
            removeMarker();
        }
        else {
            if (!isEmpty(buttonClicked)) {
                if ((this.turn % 2 == 0 && buttonClicked.figureType.color == Color.BLACK || this.turn % 2 != 0 && buttonClicked.figureType.color == Color.WHITE)
                        && previousClickedFigureButton.figureType.color != buttonClicked.figureType.color) {
                    markAvailablePos(previousClickedFigureButton);
                    moveIfAllowed(previousClickedFigureButton, buttonClicked);
                    return;
                }

                if (!previousClickedFigureButton.wasMovedAway())
                    previousClickedFigureButton.setBackground(Color.WHITE);
                buttonClicked.setBackground(Color.blue);
                if (this.turn % 2 == 0 && buttonClicked.figureType.color == Color.WHITE || this.turn % 2 != 0 && buttonClicked.figureType.color == Color.BLACK)
                    markAvailablePos(buttonClicked);

                previousClickedFigureButton = buttonClicked;
            }
            else {
                if (this.turn % 2 == 0 && previousClickedFigureButton.figureType.color == Color.WHITE || this.turn % 2 != 0 && previousClickedFigureButton.figureType.color == Color.BLACK)
                    moveIfAllowed(previousClickedFigureButton, buttonClicked);
            }
        }
    }

    private void moveIfAllowed(ChessFieldButton previousClickedFigureButton, ChessFieldButton buttonClicked) {
        if (this.markedPos != null) {
            for (Position markedPo : this.markedPos) {
                if (markedPo.equals(buttonClicked.getPos())) {
                    this.moveFigure(previousClickedFigureButton.getPos(), buttonClicked.getPos());
                    break;
                }
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

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
/*
        game.moveFigure(new Position(6,0), new Position(4, 0));
        game.moveIfWanted();
        game.turn++;

 */
        //game.markAvailablePos(game.grid[7][0]);
        //System.out.println(game.grid[0][7].figureType);
        //System.out.println(game);

        //System.out.println(game.getGrid());
        //game.printGrid();
    }
}