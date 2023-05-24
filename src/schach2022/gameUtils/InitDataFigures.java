package schach2022.gameUtils;

import java.util.HashMap;
import java.util.Map;

public record InitDataFigures() {
    public static final Map<Position, ChessFigure> PIECES;
    static {
        PIECES = new HashMap<>();
        PIECES.put(new Position(0, 0), ChessFigure.ROOK_BLACK);
        PIECES.put(new Position(7, 0), ChessFigure.ROOK_WHITE);
        PIECES.put(new Position(0, 7), ChessFigure.ROOK_BLACK);
        PIECES.put(new Position(7, 7), ChessFigure.ROOK_WHITE);

        PIECES.put(new Position(7, 4), ChessFigure.KING_WHITE);
        PIECES.put(new Position(0, 4), ChessFigure.KING_BLACK);

        PIECES.put(new Position(7, 3), ChessFigure.QUEEN_WHITE);
        PIECES.put(new Position(0, 3), ChessFigure.QUEEN_BLACK);

        PIECES.put(new Position(0, 2), ChessFigure.BISHOP_BLACK);
        PIECES.put(new Position(7, 2), ChessFigure.BISHOP_WHITE);
        PIECES.put(new Position(0, 5), ChessFigure.BISHOP_BLACK);
        PIECES.put(new Position(7, 5), ChessFigure.BISHOP_WHITE);

        PIECES.put(new Position(0, 1), ChessFigure.KNIGHT_BLACK);
        PIECES.put(new Position(7, 1), ChessFigure.KNIGHT_WHITE);
        PIECES.put(new Position(0, 6), ChessFigure.KNIGHT_BLACK);
        PIECES.put(new Position(7, 6), ChessFigure.KNIGHT_WHITE);
    }
}