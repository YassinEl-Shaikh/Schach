package schach2022.gameUtils;

import schach2022.client.GameClient;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ChessBoard {
    public static final int BUTTON_SIZE = 100;
    public static final Color BLACK = new Color(181,136,99);
    public static final Color WHITE = new Color(240,217,181);
    public static final Color YELLOW_WHITE = new Color(248,236,90);
    public static final Color YELLOW_BLACK = new Color(218,196,49);
    private ChessFieldButton[][] grid;
    private List<Position> movedMarkerPos;

    private List<Position> legalMovesMarkerPos;

    public static ChessBoard generateServerField() {
        return new ChessBoard(null);
    }
    public static ChessBoard createClientField(GameClient listener) {
        return new ChessBoard(listener);
    }
    private ChessBoard(GameClient listener) {
        this.movedMarkerPos = new ArrayList<>();
        this.legalMovesMarkerPos = new ArrayList<>();
        this.initiateField(listener);
    }
    private void initiateField(GameClient listener) {
        this.grid = new ChessFieldButton[8][8];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                this.grid[i][j] = (new ChessFieldButton(new Position(i, j), ChessFigure.EMPTY, listener));
            }
        }
        Arrays.stream(this.grid[1]).forEach(e -> e.setFigure(ChessFigure.PAWN_BLACK));
        Arrays.stream(this.grid[6]).forEach(e -> e.setFigure(ChessFigure.PAWN_WHITE));

        for (Position nextPos : InitDataFigures.PIECES.keySet()) {
            this.getButton(nextPos).figureType = InitDataFigures.PIECES.get(nextPos);
        }

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                this.grid[i][j].setIcon();
                this.grid[i][j].setBackground(i % 2 == j % 2 ? WHITE : BLACK);
                this.grid[i][j].setBorder(null);
            }
        }
    }

    public void moveFigure(Position origin, Position dest) {
        this.removeMarker();
        this.movedMarkerPos.add(origin);
        this.movedMarkerPos.add(dest);

        ChessFieldButton originButton = this.getButton(origin);
        ChessFieldButton destButton = this.getButton(dest);

        destButton.setFigure(originButton.getFigureType());
        destButton.setTouched(true);
        destButton.setIcon();
        destButton.setBackgroundToMoved();
        destButton.setMovedTo(true);

        originButton.setFigure(ChessFigure.EMPTY);
        originButton.setTouched(false);
        originButton.setIcon();
        originButton.setBackgroundToMoved();
        originButton.setMovedAway(true);

    }

    public ChessFieldButton getButton(Position p) {
        return this.grid[p.getX()][p.getY()];
    }
    private void removeMarker() {
        for (Position p: this.movedMarkerPos) {
            if (this.getButton(p).wasMovedTo()) {
                this.getButton(p).setBackground(p.getX() % 2 == p.getY() % 2 ? WHITE : BLACK);
                this.getButton(p).setMovedTo(false);
            }

            if (this.getButton(p).wasMovedAway()) {
                this.getButton(p).setBackground(p.getX() % 2 == p.getY() % 2 ? WHITE : BLACK);
                this.getButton(p).setMovedAway(false);
            }
        }
        this.movedMarkerPos.clear();
    }

    public void markLegalMoves(List<Position> legalPos) {
        for (Position p: this.legalMovesMarkerPos) {
            this.getButton(p).setAvailable(false);
        }
        for (Position p : legalPos) {
            this.getButton(p).setAvailable(true);
        }
        this.legalMovesMarkerPos = legalPos;
    }

    public boolean isMyPiece(ChessFieldButton piece, boolean isPlayer1) {
        return piece.getFigureType().color == ((isPlayer1) ? Color.WHITE : Color.BLACK);
    }
    public ChessFieldButton[][] getGrid() {
        return grid;
    }
}