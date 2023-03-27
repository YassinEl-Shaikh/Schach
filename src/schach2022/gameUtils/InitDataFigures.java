package schach2022.gameUtils;

import java.util.ArrayList;
import java.util.List;

public record InitDataFigures() {
    public static final List<List<PositionFigureWrapper>> PIECES;
    static {
        PIECES = new ArrayList<>();
        final List<PositionFigureWrapper> ROOK = new ArrayList<>();
        final List<PositionFigureWrapper> KING = new ArrayList<>();
        final List<PositionFigureWrapper> QUEEN = new ArrayList<>();
        final List<PositionFigureWrapper> BISHOP = new ArrayList<>();
        final List<PositionFigureWrapper> KNIGHT = new ArrayList<>();
        ROOK.add(new PositionFigureWrapper(new Position(0, 0), ChessFigure.ROOK_BLACK));
        ROOK.add(new PositionFigureWrapper(new Position(7, 0), ChessFigure.ROOK_WHITE));
        ROOK.add(new PositionFigureWrapper(new Position(0, 7), ChessFigure.ROOK_BLACK));
        ROOK.add(new PositionFigureWrapper(new Position(7, 7), ChessFigure.ROOK_WHITE));

        KING.add(new PositionFigureWrapper(new Position(7, 4), ChessFigure.KING_WHITE));
        KING.add(new PositionFigureWrapper(new Position(0, 4), ChessFigure.KING_BLACK));

        QUEEN.add(new PositionFigureWrapper(new Position(7, 3), ChessFigure.QUEEN_WHITE));
        QUEEN.add(new PositionFigureWrapper(new Position(0, 3), ChessFigure.QUEEN_BLACK));

        BISHOP.add(new PositionFigureWrapper(new Position(0, 2), ChessFigure.BISHOP_BLACK));
        BISHOP.add(new PositionFigureWrapper(new Position(7, 2), ChessFigure.BISHOP_WHITE));
        BISHOP.add(new PositionFigureWrapper(new Position(0, 5), ChessFigure.BISHOP_BLACK));
        BISHOP.add(new PositionFigureWrapper(new Position(7, 5), ChessFigure.BISHOP_WHITE));

        KNIGHT.add(new PositionFigureWrapper(new Position(0, 1), ChessFigure.KNIGHT_BLACK));
        KNIGHT.add(new PositionFigureWrapper(new Position(7, 1), ChessFigure.KNIGHT_WHITE));
        KNIGHT.add(new PositionFigureWrapper(new Position(0, 6), ChessFigure.KNIGHT_BLACK));
        KNIGHT.add(new PositionFigureWrapper(new Position(7, 6), ChessFigure.KNIGHT_WHITE));

        PIECES.add(ROOK);
        PIECES.add(KING);
        PIECES.add(QUEEN);
        PIECES.add(BISHOP);
        PIECES.add(KNIGHT);
    }
}