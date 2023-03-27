package schach2022.gameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class ChessFieldButton extends JButton implements ActionListener {
    private final Position pos;
    public ChessFigure figureType;
    private boolean touched;
    private boolean movedAwayFrom;

    private boolean movedTo;

    public ChessFieldButton() {
        this(null, null, null);
    }

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
        this.movedTo = false;
    }

    public void setIcon() {
        switch (figureType) {
            case KING_BLACK -> this.setIcon(schach2022.offline.FigureIcons.KING_BLACK);
            case KING_WHITE -> this.setIcon(schach2022.offline.FigureIcons.KING_WHITE);

            case QUEEN_BLACK -> this.setIcon(schach2022.offline.FigureIcons.QUEEN_BLACK);
            case QUEEN_WHITE -> this.setIcon(schach2022.offline.FigureIcons.QUEEN_WHITE);

            case ROOK_BLACK -> this.setIcon(schach2022.offline.FigureIcons.ROOK_BLACK);
            case ROOK_WHITE -> this.setIcon(schach2022.offline.FigureIcons.ROOK_WHITE);

            case KNIGHT_BLACK -> this.setIcon(schach2022.offline.FigureIcons.KNIGHT_BLACK);
            case KNIGHT_WHITE -> this.setIcon(schach2022.offline.FigureIcons.KNIGHT_WHITE);

            case BISHOP_BLACK -> this.setIcon(schach2022.offline.FigureIcons.BISHOP_BLACK);
            case BISHOP_WHITE -> this.setIcon(schach2022.offline.FigureIcons.BISHOP_WHITE);

            case PAWN_BLACK -> this.setIcon(schach2022.offline.FigureIcons.PAWN_BLACK);
            case PAWN_WHITE -> this.setIcon(FigureIcons.PAWN_WHITE);

            case EMPTY -> {
                this.setIcon(null); this.setBackground(Color.WHITE);
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
}