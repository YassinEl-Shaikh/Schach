package schach2022;

import java.awt.*;

public enum ChessFigure {

    EMPTY,
    KING_WHITE(Color.WHITE),
    QUEEN_WHITE(Color.WHITE),
    ROOK_WHITE(Color.WHITE),
    BISHOP_WHITE(Color.WHITE),
    KNIGHT_WHITE(Color.WHITE),
    PAWN_WHITE(Color.WHITE),
    KING_BLACK(Color.BLACK),
    QUEEN_BLACK(Color.BLACK),
    ROOK_BLACK(Color.BLACK),
    BISHOP_BLACK(Color.BLACK),
    KNIGHT_BLACK(Color.BLACK),
    PAWN_BLACK(Color.BLACK);

    Color color;

    ChessFigure() {
    }

    ChessFigure(Color color) {
        this.color = color;
    }
}