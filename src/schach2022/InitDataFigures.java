package schach2022;

import java.util.ArrayList;
import java.util.List;

public record InitDataFigures() {
    private final static List<List<PositionFigureWrapper>> PIECES = new ArrayList<>();
    private final static List<PositionFigureWrapper> ROOK = new ArrayList<>();
    private final static List<PositionFigureWrapper> KING = new ArrayList<>();
    private final static List<PositionFigureWrapper> QUEEN = new ArrayList<>();
    private final static List<PositionFigureWrapper> BISHOP = new ArrayList<>();
    private final static List<PositionFigureWrapper> KNIGHT = new ArrayList<>();

    public static void init() {
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

    public static List<List<PositionFigureWrapper>> get() {
        return PIECES;
    }
}