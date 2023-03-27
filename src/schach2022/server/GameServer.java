package schach2022.server;

import schach2022.gameUtils.*;
import schach2022.utils.*;
import schach2022.communication.SerializingTools;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameServer {
    /*
    private final List<List<PositionFigureWrapper>> initData;
    private final ChessFieldButton[][] grid;
    private long turn;

    private ChessFieldButton movedFromMarkerButton;
    private ChessFieldButton movedToMarkerButton;
    private List<Position> markedPos;

    private ChessFieldButton previousClickedFigureButton;

    private Socket player1;
    private Socket player2;

    private InputStream player1Input;
    private InputStream player2Input;

    private OutputStream player1Output;
    private OutputStream player2Output;
    private final ServerSocket socket;
    private boolean running = false;

    public GameServer() throws IOException {
        this.socket = new ServerSocket(1337);
        this.turn = 0;
        this.markedPos = new ArrayList<>();
        this.movedFromMarkerButton = new ChessFieldButton();
        this.movedToMarkerButton = new ChessFieldButton();
        this.grid = new ChessFieldButton[8][8];

        InitDataFigures.init();
        this.initData = InitDataFigures.get();
        this.initiateField();
    }

    private void connectPlayers() {
        try {
            this.player1 = socket.accept();
        }
        catch (IOException e) {
            throw new FailedToConnectException("Connection of Player1 failed!");
        }
        try {
            this.player2 = socket.accept();
        }
        catch (IOException e) {
            throw new FailedToConnectException("Connection of Player2 failed!");
        }
    }
    public void run() throws InterruptedException, IOException {
        this.connectPlayers();

        this.player1Input = this.player1.getInputStream();
        this.player2Input = this.player2.getInputStream();

        this.player1Output = this.player1.getOutputStream();
        this.player2Output = this.player2.getOutputStream();
        this.running = true;
        do {
            getAndExecutePlayerOrders();
            if (checkWon())
                this.running = false;

            Thread.sleep((long) (1000/10d));
        } while(this.running);
    }

    private boolean checkWon() {
        return false;
    }

    private void getAndExecutePlayerOrders() {
        try {
            boolean player1 = this.turn % 2 == 0;
            List<Position> pos = new ArrayList<>();
            if ((player1) ? this.player1Input.available() > 0 : this.player2Input.available() > 0)
                pos = SerializingTools.deSerialize(Arrays.stream(SerializingTools.box((player1) ? this.player1Input.readAllBytes() : this.player2Input.readAllBytes())));

            ChessFieldButton buttonClicked = this.grid[pos.get(0).getX()][pos.get(0).getY()];
            if (previousClickedFigureButton == null)
                previousClickedFigureButton = buttonClicked;


            if (this.player1Input.readNBytes(1)[0] == Operation.move.sequence) {
                if (buttonClicked.getBackground() == Color.blue) {
                    buttonClicked.setBackground(Color.WHITE);
                    sendCommand((player1) ? this.player1 : this.player2, Operation.drawWHITE, List.of(buttonClicked.getPos()));
                }
                else {
                    if (!isEmpty(buttonClicked)) {
                        if ((this.turn % 2 == 0 && buttonClicked.figureType.color == Color.BLACK || this.turn % 2 != 0 && buttonClicked.figureType.color == Color.WHITE)
                                && previousClickedFigureButton.figureType.color != buttonClicked.figureType.color) {
                            markAvailablePos(previousClickedFigureButton);
                            moveIfAllowed(previousClickedFigureButton, buttonClicked);
                            return;
                        }

                        if (!previousClickedFigureButton.wasMovedAway())
                            previousClickedFigureButton.setBackground(Color.WHITE);
                        buttonClicked.setBackground(Color.blue);
                        sendCommand((player1) ? this.player1 : this.player2, List.of(buttonClicked.getPos()));
                        if (this.turn % 2 == 0 && buttonClicked.figureType.color == Color.WHITE || this.turn % 2 != 0 && buttonClicked.figureType.color == Color.BLACK)
                            markAvailablePos(buttonClicked);

                        previousClickedFigureButton = buttonClicked;
                    }
                    else {
                        if (this.turn % 2 == 0 && previousClickedFigureButton.figureType.color == Color.WHITE || this.turn % 2 != 0 && previousClickedFigureButton.figureType.color == Color.BLACK)
                            moveIfAllowed(previousClickedFigureButton, buttonClicked);
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendCommand(Socket player List<Position> list) throws IOException {
        DataOutputStream dOS = new DataOutputStream(player.getOutputStream());
        OutputStream oS = player.getOutputStream();

        dOS.writeBytes(Arrays.toString(SerializingTools.serialize(list).toArray(Byte[]::new)));
    }

    private void initiateField() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                this.grid[i][j] = new ChessFieldButton(new Position(i, j), ChessFigure.EMPTY);
            }
        }
        Arrays.stream(this.grid[1]).forEach(e -> e.setFigure(ChessFigure.PAWN_BLACK));
        Arrays.stream(this.grid[6]).forEach(e -> e.setFigure(ChessFigure.PAWN_WHITE));

        for (List<PositionFigureWrapper> initDatum : this.initData) {
            for (PositionFigureWrapper pFW : initDatum) {
                Position nextPos = pFW.position();
                this.grid[nextPos.getX()][nextPos.getY()].figureType = pFW.figureType();// = new ChessFieldButton(nextPos, pFW.figureType());
            }
        }
    }

    private void moveFigure(Position origin, Position dest) throws IOException {
        this.movedFromMarkerButton.setMovedAway(false);
        this.movedFromMarkerButton.setBackground(Color.WHITE);
        this.movedToMarkerButton.setMovedTo(false);
        this.movedToMarkerButton.setBackground(Color.WHITE);
        ChessFieldButton originButton = this.grid[origin.getX()][origin.getY()];
        ChessFieldButton destButton = this.grid[dest.getX()][dest.getY()];
        ChessFigure toMove = this.grid[origin.getX()][origin.getY()].getFigureType();

        originButton.setFigure(ChessFigure.EMPTY);
        originButton.setBackground(Color.GREEN);
        originButton.setMovedAway(true);
        sendCommand((this.turn % 2 == 0) ? player1 : player2, Operation.drawGREEN, List.of(originButton.getPos()));

        destButton.setFigure(toMove);
        destButton.setTouched(true);
        destButton.setMovedTo(true);
        destButton.setBackground(Color.PINK);
        sendCommand((this.turn % 2 == 0) ? player1 : player2, Operation.drawPINK, List.of(destButton.getPos()));

        this.movedFromMarkerButton = originButton;
        this.movedToMarkerButton = destButton;

        this.removeMarker();
        this.previousClickedFigureButton = null;
        this.turn++;
    }

    private boolean isEmpty(ChessFieldButton button) {
        return button.figureType == ChessFigure.EMPTY;
    }

    private List<Position> getRookPositions(ChessFieldButton buttonClicked, int buttonX, int buttonY) {
        List<Position> result = new ArrayList<>();
        for (int i = buttonY + 1; i < this.grid.length; i++) {
            if (!isEmpty(this.grid[buttonX][i])) {
                if (buttonClicked.figureType.color != this.grid[buttonX][i].figureType.color)
                    result.add(new Position(buttonX, i));
                break;
            }
            else
                result.add(new Position(buttonX, i));
        }
        for (int i = buttonY - 1; i >= 0; i--) {
            if (!isEmpty(this.grid[buttonX][i])) {
                if (buttonClicked.figureType.color != this.grid[buttonX][i].figureType.color)
                    result.add(new Position(buttonX, i));
                break;
            }
            else
                result.add(new Position(buttonX, i));
        }

        for (int i = buttonX + 1; i < this.grid.length; i++) {
            if (!isEmpty(this.grid[i][buttonY])) {
                if (buttonClicked.figureType.color != this.grid[i][buttonY].figureType.color)
                    result.add(new Position(i, buttonY));
                break;
            }
            else
                result.add(new Position(i, buttonY));
        }
        for (int i = buttonX - 1; i >= 0; i--) {
            if (!isEmpty(this.grid[i][buttonY])) {
                if (buttonClicked.figureType.color != this.grid[i][buttonY].figureType.color)
                    result.add(new Position(i, buttonY));
                break;
            }
            else
                result.add(new Position(i, buttonY));
        }
        return result;
    }
    private List<Position> getBishopPositions(ChessFieldButton buttonClicked, int buttonX, int buttonY) {
        List<Position> result = new ArrayList<>();
        for (int i = buttonX + 1, j = buttonY +1; i < this.grid.length && j < this.grid.length; i++, j++) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }
        for (int i = buttonX - 1, j = buttonY -1; i >= 0 && j >= 0; i--, j--) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }

        for (int i = buttonX + 1, j = buttonY -1; i < this.grid.length && j >= 0; i++, j--) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }
        for (int i = buttonX - 1, j = buttonY +1; i >= 0 && j < this.grid.length; i--, j++) {
            if (!isEmpty(this.grid[i][j])) {
                if (buttonClicked.figureType.color != this.grid[i][j].figureType.color)
                    result.add(new Position(i, j));
                break;
            }
            else
                result.add(new Position(i, j));
        }
        return result;
    }
    private List<Position> findAvailablePos(ChessFieldButton buttonClicked) {
        List<Position> result = new ArrayList<>();

        if (buttonClicked.figureType == ChessFigure.EMPTY)
            return null;

        int buttonX = buttonClicked.getPos().getX();
        int buttonY = buttonClicked.getPos().getY();

        switch (buttonClicked.figureType) {
            case ROOK_WHITE, ROOK_BLACK:

                result = getRookPositions(buttonClicked, buttonX, buttonY);

                break;

            case BISHOP_WHITE, BISHOP_BLACK:

                result = getBishopPositions(buttonClicked, buttonX, buttonY);

                break;

            case QUEEN_WHITE, QUEEN_BLACK:

                result.addAll(getRookPositions(buttonClicked, buttonX, buttonY));
                result.addAll(getBishopPositions(buttonClicked, buttonX, buttonY));

                break;

            case KNIGHT_WHITE, KNIGHT_BLACK:
                if (buttonX > 1 && buttonY < this.grid.length -1 && (isEmpty(this.grid[buttonX -2][buttonY +1])
                        || this.grid[buttonX -2][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -2, buttonY +1));

                if (buttonX > 1 && buttonY > 0 && (buttonY < this.grid.length && isEmpty(this.grid[buttonX -2][buttonY -1])
                        || this.grid[buttonX -2][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -2, buttonY -1));

                // Movement up -> left and right

                if (buttonX < this.grid.length -2 && buttonY < this.grid.length -1 && (isEmpty(this.grid[buttonX +2][buttonY +1])
                        || this.grid[buttonX +2][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +2, buttonY +1));

                if (buttonX < this.grid.length -2 && buttonY > 0 && (buttonY < this.grid.length && isEmpty(this.grid[buttonX +2][buttonY -1])
                        || this.grid[buttonX +2][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +2, buttonY -1));

                // Movement down -> left and right

                if (buttonX < this.grid.length -1 && buttonY < this.grid.length -2 && (isEmpty(this.grid[buttonX +1][buttonY +2])
                        || this.grid[buttonX +1][buttonY +2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY +2));

                if (buttonX > 0 && buttonY < this.grid.length -2 && (isEmpty(this.grid[buttonX -1][buttonY +2])
                        || this.grid[buttonX -1][buttonY +2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY +2));

                // Movement right -> up and down

                if (buttonX < this.grid.length -1 && buttonY > 1 && (isEmpty(this.grid[buttonX +1][buttonY -2])
                        || this.grid[buttonX +1][buttonY -2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY -2));

                if (buttonX > 0 && buttonY > 1 && (isEmpty(this.grid[buttonX -1][buttonY -2])
                        || this.grid[buttonX -1][buttonY -2].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY -2));

                // Movement left -> up and down

                break;

            case KING_WHITE, KING_BLACK:
                if (buttonX < this.grid.length-1 && (isEmpty(this.grid[buttonX +1][buttonY])
                        || this.grid[buttonX +1][buttonY].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY));

                if (buttonX < this.grid.length-1 && (buttonY < this.grid.length && isEmpty(this.grid[buttonX +1][buttonY +1])
                        || this.grid[buttonX +1][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY +1));

                if (buttonX < this.grid.length-1 && (isEmpty(this.grid[buttonX][buttonY +1])
                        || this.grid[buttonX][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX, buttonY +1));

                if (buttonX > 0 && (isEmpty(this.grid[buttonX -1][buttonY])
                        || this.grid[buttonX -1][buttonY].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY));


                if (buttonX > 0 && buttonY > 0 && (isEmpty(this.grid[buttonX -1][buttonY -1])
                        || this.grid[buttonX -1][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY -1));

                if (buttonY > 0 && (isEmpty(this.grid[buttonX][buttonY -1])
                        || this.grid[buttonX][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX, buttonY -1));

                if (buttonX > 0 && buttonY < this.grid.length-1 && (isEmpty(this.grid[buttonX -1][buttonY +1])
                        || this.grid[buttonX -1][buttonY +1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX -1, buttonY +1));

                if (buttonX < this.grid.length-1 && buttonY > 0 && (isEmpty(this.grid[buttonX +1][buttonY -1])
                        || this.grid[buttonX +1][buttonY -1].figureType.color != buttonClicked.figureType.color))
                    result.add(new Position(buttonX +1, buttonY -1));
                break;

            case PAWN_WHITE, PAWN_BLACK:
                if (buttonClicked.figureType == ChessFigure.PAWN_BLACK) {
                    for (int i = buttonX +1; i < this.grid.length; i++) {
                        if (result.size() > 1 || !isEmpty(this.grid[i][buttonY]))
                            break;
                        else {
                            if (result.size() == 1 && buttonClicked.isTouched())
                                break;
                            result.add(new Position(i, buttonY));
                        }
                    }
                }
                else {
                    for (int i = buttonX - 1; i > 0; i--) {
                        if (result.size() > 1 || !isEmpty(this.grid[i][buttonY]))
                            break;
                        else {
                            if (result.size() == 1 && buttonClicked.isTouched())
                                break;
                            result.add(new Position(i, buttonY));
                        }
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Clicked on Empty field!");
        }
        return result;
    }

    private void removeMarker() throws IOException {
        for (Position p: this.markedPos) {
            if (this.grid[p.getX()][p.getY()].wasMovedTo()) {
                this.grid[p.getX()][p.getY()].setBackground(Color.PINK);
                sendCommand((this.turn % 2 == 0) ? this.player2 : this.player1, Operation.drawPINK, List.of(p));
                continue;
            }
            else {
                this.grid[p.getX()][p.getY()].setBackground(Color.WHITE);
                sendCommand((this.turn % 2 == 0) ? this.player1 : this.player2, Operation.drawWHITE, List.of(p));
            }

            if (this.grid[p.getX()][p.getY()].wasMovedAway()) {
                this.grid[p.getX()][p.getY()].setBackground(Color.GREEN);
                sendCommand((this.turn % 2 == 0) ? this.player2 : this.player1, Operation.drawWHITE, List.of(p));
            }
            else {
                this.grid[p.getX()][p.getY()].setBackground(Color.WHITE);
                sendCommand((this.turn % 2 == 0) ? this.player1 : this.player2, Operation.drawWHITE, List.of(p));
            }
        }
    }
    private void markAvailablePos(ChessFieldButton button) throws IOException {
        List<Position> positions = this.findAvailablePos(button);

        this.removeMarker();

        if (positions == null)
            return;
        for (Position p : positions) {
            this.grid[p.getX()][p.getY()].setBackground(Color.GRAY);
            sendCommand((this.turn % 2 == 0) ? this.player1 : this.player2, Operation.drawGRAY, List.of(p));
        }
        this.markedPos = new ArrayList<>(positions);
    }
    private void moveIfAllowed(ChessFieldButton previousClickedFigureButton, ChessFieldButton buttonClicked) throws IOException {
        if (this.markedPos != null) {
            for (Position markedPo : this.markedPos) {
                if (markedPo.equals(buttonClicked.getPos())) {
                    this.moveFigure(previousClickedFigureButton.getPos(), buttonClicked.getPos());
                    break;
                }
            }
        }
    }

     */
}