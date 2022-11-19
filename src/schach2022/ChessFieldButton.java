package schach2022;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class ChessFieldButton extends JButton implements ActionListener {

    private final static String PATH = "C:/Users/yassi/IdeaProjects/Home (new JDK)/src/schachNeu/Icons/";
    private final static ImageIcon ROOK_BLACK = new ImageIcon(PATH + "Rook_Black.png");
    private final static ImageIcon ROOK_WHITE = new ImageIcon(PATH + "Rook_White.png");
    private final static ImageIcon KING_BLACK = new ImageIcon(PATH + "King_Black.png");
    private final static ImageIcon KING_WHITE = new ImageIcon(PATH + "King_White.png");
    private final static ImageIcon QUEEN_BLACK = new ImageIcon(PATH + "Queen_Black.png");
    private final static ImageIcon QUEEN_WHITE = new ImageIcon(PATH + "Queen_White.png");
    private final static ImageIcon PAWN_BLACK = new ImageIcon(PATH + "Pawn_Black.png");
    private final static ImageIcon PAWN_WHITE = new ImageIcon(PATH + "Pawn_White.png");
    private final static ImageIcon KNIGHT_BLACK = new ImageIcon(PATH + "Knight_Black.png");
    private final static ImageIcon KNIGHT_WHITE = new ImageIcon(PATH + "Knight_White.png");
    private final static ImageIcon BISHOP_BLACK = new ImageIcon(PATH + "Bishop_Black.png");
    private final static ImageIcon BISHOP_WHITE = new ImageIcon(PATH + "Bishop_White.png");
    private final Position pos;
    ChessFigure figureType;
    private boolean touched;

    private boolean movedAwayFrom;

    public ChessFieldButton(Position pos, ChessFigure figureType, MouseListener l) {
        this.pos = pos;
        this.figureType = figureType;
        this.setSize(100, 100);
        this.setVisible(true);
        this.addMouseListener(l);
        this.addActionListener(this);
        this.setBackground(Color.WHITE);
        this.touched = false;
        this.movedAwayFrom = false;
    }

    public void setIcon() {
        switch (figureType) {
            case KING_BLACK -> this.setIcon(KING_BLACK);
            case KING_WHITE -> this.setIcon(KING_WHITE);

            case QUEEN_BLACK -> this.setIcon(QUEEN_BLACK);
            case QUEEN_WHITE -> this.setIcon(QUEEN_WHITE);

            case ROOK_BLACK -> this.setIcon(ROOK_BLACK);
            case ROOK_WHITE -> this.setIcon(ROOK_WHITE);

            case KNIGHT_BLACK -> this.setIcon(KNIGHT_BLACK);
            case KNIGHT_WHITE -> this.setIcon(KNIGHT_WHITE);

            case BISHOP_BLACK -> this.setIcon(BISHOP_BLACK);
            case BISHOP_WHITE -> this.setIcon(BISHOP_WHITE);

            case PAWN_BLACK -> this.setIcon(PAWN_BLACK);
            case PAWN_WHITE -> this.setIcon(PAWN_WHITE);

            case EMPTY -> {
                this.setIcon(null); this.setBackground(Color.WHITE);
            }
        }
    }
    public ChessFigure getFigureType() {
        return figureType;
    }

    public void setMoved(boolean value) {
        this.movedAwayFrom = value;
    }
    public boolean wasMoved() {
        return this.movedAwayFrom;
    }
    public void setFigure(ChessFigure figureType) {
        this.figureType = figureType;
        this.setIcon();
    }

    @Override
    public String toString() {
        return figureType + ", " + touched + "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Pos: " + this.pos + " | type: " + this.figureType + ", " + this.touched);
    }

    public Position getPos() {
        return pos;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public boolean isTouched() {
        return touched;
    }

    /*
    public static void main(String[] args) throws IOException {
        //resizeImage(ImageIO.read(new File("C:/Users/yassi/IdeaProjects/Home (new JDK)/src/schachNeu/Icons/King_Black.png")));
    }

    private static void resizeImage(BufferedImage originalImage) throws IOException {
        BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, 100, 100, null);
        graphics2D.dispose();
        ImageIO.write(resizedImage, "png", new File("C:/Users/yassi/IdeaProjects/Home (new JDK)/src/schachNeu/Icons/King_Black.png"));

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (previousClickedFigureButton == null)
            previousClickedFigureButton = this;
        if (previousClickedEmptyButton == null)
            previousClickedEmptyButton = this;

        if (this.figureType == ChessFigure.EMPTY || this.figureType == ChessFigure.EMPTY_MOVED_BLACK || this.figureType == ChessFigure.EMPTY_MOVED_WHITE) {
            if (this.getBackground() == Color.DARK_GRAY) {
                this.setBackground(Color.WHITE);
                this.target = null;
            }
            else {
                if (previousClickedEmptyButton.getBackground() != Color.BLUE)
                    previousClickedEmptyButton.setBackground(Color.WHITE);
                this.setBackground(Color.DARK_GRAY);
                this.target = this;
                this.wantsToMove = true;
                previousClickedEmptyButton = this;

            }
        }
        else {
            previousClickedEmptyButton.setBackground(Color.WHITE);
            if (this.getBackground() == Color.blue) {
                this.setBackground(Color.WHITE);
                this.target = null;
                this.wantsToMove = false;
            }
            else {
                previousClickedFigureButton.setBackground(Color.WHITE);
                this.setBackground(Color.blue);
                previousClickedFigureButton = this;
                this.wantsToMove = true;
            }
        }
        System.out.println(this.target + " : " + this.wantsToMove);
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

 */
}