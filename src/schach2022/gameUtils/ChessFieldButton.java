package schach2022.gameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class ChessFieldButton extends JButton {
    private final Position pos;
    public ChessFigure figureType; // Will ich das wirklich public ?
    private boolean touched;
    private boolean movedAwayFrom;
    private boolean movedTo;
    private boolean selected;

    public ChessFieldButton() {
        this(null, null, null);
    }

    public ChessFieldButton(Position pos, ChessFigure figureType, MouseListener l) {
        this.pos = pos;
        this.figureType = figureType;
        this.setSize(100, 100);
        this.setVisible(true);
        this.addMouseListener(l);
        this.touched = false;
        this.movedAwayFrom = false;
        this.movedTo = false;
        this.selected = false;
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setIcon() {
        switch (figureType) {
            case KING_BLACK -> this.setIcon(FigureIcons.KING_BLACK);
            case KING_WHITE -> this.setIcon(FigureIcons.KING_WHITE);

            case QUEEN_BLACK -> this.setIcon(FigureIcons.QUEEN_BLACK);
            case QUEEN_WHITE -> this.setIcon(FigureIcons.QUEEN_WHITE);

            case ROOK_BLACK -> this.setIcon(FigureIcons.ROOK_BLACK);
            case ROOK_WHITE -> this.setIcon(FigureIcons.ROOK_WHITE);

            case KNIGHT_BLACK -> this.setIcon(FigureIcons.KNIGHT_BLACK);
            case KNIGHT_WHITE -> this.setIcon(FigureIcons.KNIGHT_WHITE);

            case BISHOP_BLACK -> this.setIcon(FigureIcons.BISHOP_BLACK);
            case BISHOP_WHITE -> this.setIcon(FigureIcons.BISHOP_WHITE);

            case PAWN_BLACK -> this.setIcon(FigureIcons.PAWN_BLACK);
            case PAWN_WHITE -> this.setIcon(FigureIcons.PAWN_WHITE);

            case EMPTY -> {
                this.setIcon(null);
            }
        }
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setBackgroundToSelected() {
        this.setBackground(this.pos.getX() % 2 == this.pos.getY() % 2 ? ChessBoard.YELLOW_WHITE : ChessBoard.YELLOW_BLACK);
    }
    public void setBackgroundToOrigin() {
        this.setBackground(this.pos.getX() % 2 == this.pos.getY() % 2 ? ChessBoard.WHITE : ChessBoard.BLACK);
    }
    public void setBackgroundToMoved() {
        this.setBackground(this.pos.getX() % 2 == this.pos.getY() % 2 ? ChessBoard.YELLOW_WHITE : ChessBoard.YELLOW_BLACK);
    }
    public boolean equals(ChessFieldButton other) {
        return this.pos.equals(other.pos);
    }

    public boolean isWhite() {
        return this.figureType.color.equals(Color.WHITE);
    }

    public boolean isEmpty() {
        return this.figureType.equals(ChessFigure.EMPTY);
    }

    public void setAvailable(boolean show) {
        if (show)
            if ((this.figureType.equals(ChessFigure.EMPTY)))
                this.setIcon ((this.pos.getX() % 2 == this.pos.getY() % 2 ? FigureIcons.LEGAL_MOVE_WHITE : FigureIcons.LEGAL_MOVE_BLACK));
            else {
                this.setBackground(Color.LIGHT_GRAY);
            }
        else {
            if ((this.figureType.equals(ChessFigure.EMPTY)))
                this.setIcon(null);
            else {
                this.setIcon();
                this.setBackgroundToOrigin();
            }
        }
    }
    public ChessFigure getFigureType() {
        return figureType;
    }

    public void setMovedAway(boolean value) {
        this.movedAwayFrom = value;
    }
    public boolean wasMovedAway() {
        return this.movedAwayFrom;
    }
    public void setMovedTo(boolean value) {
        this.movedTo = value;
    }
    public boolean wasMovedTo() {
        return this.movedTo;
    }

    public void setFigure(ChessFigure figureType) {
        this.figureType = figureType;
        this.setIcon();
    }

    @Override
    public String toString() {
        return figureType + ", " + touched + "";
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
}