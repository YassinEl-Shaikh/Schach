package schach2022.gameUtils;

import schach2022.client.GameClient;

import java.util.Arrays;
import java.util.List;

public class FieldInitialization {
    public static ChessFieldButton[][] initiateField(GameClient listener) {
        ChessFieldButton[][] field = new ChessFieldButton[8][8];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = (new ChessFieldButton(new Position(i, j), ChessFigure.EMPTY, listener));
            }
        }
        Arrays.stream(field[1]).forEach(e -> e.setFigure(ChessFigure.PAWN_BLACK));
        Arrays.stream(field[6]).forEach(e -> e.setFigure(ChessFigure.PAWN_WHITE));


        for (List<PositionFigureWrapper> initDatum : InitDataFigures.PIECES) {
            for (PositionFigureWrapper pFW : initDatum) {
                Position nextPos = pFW.position();
                field[nextPos.getX()][nextPos.getY()].figureType = pFW.figureType();
                // = new ChessFieldButton(nextPos, pFW.figureType(), this);
            }
        }

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j].setBounds(j * 100, i * 100, 100, 100);
                //this.grid.get(i).get(j).setPos(new Position(i, j));
                field[i][j].setIcon();
            }
        }
        return field;
    }
}