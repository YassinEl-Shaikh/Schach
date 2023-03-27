package schach2022.gameUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameRules {
    private static boolean canRookMove(Position origin, Position dest, ChessFieldButton[][] field, boolean turn) {
        int oX = origin.getX();
        int oY = origin.getY();
        int dX = dest.getX();
        int dY = dest.getY();

        ChessFieldButton destButton = field[dest.getX()][dest.getY()];

        if (oX == dX) {
            for (int i = Math.min(oY, dY) +1; i < Math.max(oY, dY); i++) {
                if (!isEmpty(field[oX][i])) {
                    return false;
                }
            }
            return isEmpty(destButton) || destButton.figureType.color != (turn ? Color.WHITE : Color.BLACK);
        }
        else if (oY == dY) {
            for (int i = Math.min(oX, dX) +1; i < Math.max(oX, dX); i++) {
                if (!isEmpty(field[i][oY])) {
                    return false;
                }
            }
            return isEmpty(destButton) || destButton.figureType.color != (turn ? Color.WHITE : Color.BLACK);
        }
        return false;
    }
    private static boolean canBishopMove(Position origin, Position dest, ChessFieldButton[][] field, boolean turn) {
        int oX = origin.getX();
        int oY = origin.getY();
        int dX = dest.getX();
        int dY = dest.getY();

        ChessFieldButton destButton = field[dest.getX()][dest.getY()];
        boolean rightDown = true;
        boolean leftUp = true;
        boolean rightUp = true;
        boolean leftDown = true;

        if (Math.abs(Math.abs(oX - dX) - Math.abs(oY - dY)) == 0) {
            for (int i = Math.min(oX, dX) +1, j = Math.min(oY, dY) +1; i < Math.max(oX, dX) && j < Math.max(oY, dY); i++, j++) {
                if (!isEmpty(field[i][j])) {
                    rightDown = false;
                    break;
                }
            }
            for (int i = Math.max(oX, dX) -1, j = Math.max(oY, dY) -1; i > Math.min(oX, dX) && j > Math.min(oY, dY); i--, j--) {
                if (!isEmpty(field[i][j])) {
                    leftUp = false;
                    break;
                }
            }

            for (int i = Math.max(oX, dX) -1, j = Math.min(oY, dY) +1; i > Math.min(oX, dX) && j < Math.max(oY, dY); i--, j++) {
                if (!isEmpty(field[i][j])) {
                    rightUp = false;
                    break;
                }
            }
            for (int i = Math.min(oX, dX) +1, j = Math.max(oY, dY) -1; i < Math.max(oX, dX) && j > Math.min(oY, dY); i++, j--) {
                if (!isEmpty(field[i][j])) {
                    leftDown = false;
                    break;
                }
            }
            return (rightDown || leftUp || rightUp || leftDown) && (isEmpty(destButton) || destButton.figureType.color != (turn ? Color.WHITE : Color.BLACK));
        }
        return false;
    }

    public static boolean canMove(Position origin, Position dest, ChessFieldButton[][] field, boolean turn) {
        int oX = origin.getX();
        int oY = origin.getY();
        int dX = dest.getX();
        int dY = dest.getY();

        ChessFieldButton originButton = field[origin.getX()][origin.getY()];
        ChessFieldButton destButton = field[dest.getX()][dest.getY()];

        switch (originButton.figureType) {
            case PAWN_WHITE, PAWN_BLACK:
                if (Math.abs(oY - dY) == 0) {
                    if (originButton.isTouched()) {
                        return (oX - dX == 1 && (isEmpty(destButton)) && turn) || (oX - dX == -1 && (isEmpty(destButton)) && !turn);
                    }
                    return (oX - dX <= 2 && isEmpty(destButton) && turn) || (oX - dX >= -2 && oX - dX < 0 && isEmpty(destButton) && !turn);
                }
                return false;

            case ROOK_WHITE, ROOK_BLACK:

                return canRookMove(origin, dest, field, turn);

            case KNIGHT_WHITE, KNIGHT_BLACK:

                return (Math.abs(oX - dX) == 2 && Math.abs(oY - dY) == 1
                        || Math.abs(oY - dY) == 2 && Math.abs(oX - dX) == 1)
                        && destButton.figureType.color != (turn ? Color.WHITE : Color.BLACK);

            case BISHOP_WHITE, BISHOP_BLACK:

                return canBishopMove(origin, dest, field, turn);

            case KING_WHITE, KING_BLACK:

                return Math.abs(oX - dX) <= 1 && Math.abs(oY - dY) <= 1 &&
                        destButton.figureType.color != (turn ? Color.WHITE : Color.BLACK);

            case QUEEN_WHITE, QUEEN_BLACK:

                return canRookMove(origin, dest, field, turn) || canBishopMove(origin, dest, field, turn);

            default:
                throw new RuntimeException("LOST FigureType");
        }
    }

    public static boolean canKingMove(Position origin) {
        int x = origin.getX();
        int y = origin.getY();
        List<Position> pos = List.of(new Position(x +1, y), new Position(x -1, y), new Position(x, y +1), new Position(x, y -1),
                new Position(x +1, y +1), new Position(x -1, y -1), new Position(x +1, y +1), new Position(x +1, y +1));

        for (Position p : pos) {
            if (Math.abs(x - p.getX()) <= 1 && Math.abs(y - p.getY()) <= 1)
                return true;
        }
        return false;
    }

    public static List<Position> getAvailablePos(Position origin, ChessFieldButton[][] field, boolean turn) {
        List<Position> result = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (canMove(origin, new Position(i, j), field, turn))
                    result.add(new Position(i, j));
            }
        }
        return result;
    }

    public static List<Position> getAvailablePos(Position origin, ChessFieldButton[][] field) {
       return getAvailablePos(origin, field, field[origin.getX()][origin.getY()].figureType.color == Color.WHITE);
    }
    public static boolean isEmpty(ChessFieldButton button) {
        return button.figureType == ChessFigure.EMPTY;
    }

    public static boolean isCheckMate(ChessFieldButton king, ChessFieldButton[][] field) {
        return isInCheck(king, field) && !canKingMove(king.getPos());
    }

    public static boolean isInCheck(ChessFieldButton king, ChessFieldButton[][] field) {
        return false;
    }

    public static ChessFieldButton getKing(ChessFieldButton[][] field, Color kingColor) {
        for (ChessFieldButton[] arr : field) {
            for (ChessFieldButton button : arr) {
                if (button.figureType.color == kingColor)
                    return button;
            }
        }
        return null;
    }
}