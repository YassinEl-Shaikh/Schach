package schach2022.server;

import schach2022.client.GameClient;
import schach2022.gameUtils.*;
import schach2022.utils.*;
import schach2022.communication.SerializingTools;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class GameServerNew {
    private final ChessFieldButton[][] grid;
    private boolean isP1Turn;
    private ChessFieldButton movedFromMarkerButton;
    private ChessFieldButton movedToMarkerButton;
    private List<Position> markedPos;
    private Socket player1;
    private Socket player2;
    private final ServerSocket socket;
    private boolean running = false;

    public GameServerNew() {
        try {
            this.socket = new ServerSocket(1337);
        } catch (IOException e) {
            throw new RuntimeException("Was not able to set port: 1337");
        }
        this.isP1Turn = true;
        this.markedPos = new ArrayList<>();
        this.movedFromMarkerButton = new ChessFieldButton();
        this.movedToMarkerButton = new ChessFieldButton();
        this.grid = FieldInitialization.initiateField( null);
    }

    private void connectPlayers() {
        this.player1 = connectPlayer(1, (byte) 1);
        this.player2 = connectPlayer(2, (byte) 2);
        /*try {
            this.player1 = socket.accept();
            this.player1.getOutputStream().write(1);
            System.out.println("Player 1 is there!");
        }
        catch (IOException e) {
            throw new FailedToConnectException("Connection of Player1 failed!");
        }
        try {
            this.player2 = socket.accept();
            this.player2.getOutputStream().write(2);
            System.out.println("Player 2 is there!");
        }
        catch (IOException e) {
            throw new FailedToConnectException("Connection of Player2 failed!");
        }*/
    }

    private Socket connectPlayer(int id, byte send) {
        try {
            Socket player = socket.accept();
            OutputStream os = player.getOutputStream();
            os.write(send);
            os.flush();
            System.out.println("Player " + id +  " is there!");
            return player;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        this.connectPlayers();
        this.running = true;
        System.out.println("Players connected!");
        do {
            try {
                this.getAndExecutePlayerMoves();
            } catch (IOException e) {
                throw new RuntimeException(e + "MEINE RUNTIME");
            }

            if (GameRules.isCheckMate(GameRules.getKing(this.grid, this.isP1Turn ? ChessFigure.KING_BLACK.color : ChessFigure.KING_WHITE.color), this.grid))
                this.running = false;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while(this.running);
    }

    private boolean checkWon() {
        return false;
    }

    private void getAndExecutePlayerMoves() throws IOException {
        InputStream player1Input = this.player1.getInputStream();
        InputStream player2Input = this.player2.getInputStream();

        if (this.isP1Turn ? player1Input.available() == 0 : player2Input.available() == 0)
            return;

        PositionTuple pos = SerializingTools.deSerialize(Arrays.stream(SerializingTools.box(this.isP1Turn ? player1Input.readNBytes(2) : player2Input.readNBytes(2))));
        System.out.println("Server deserialized: " + pos.getOrigin() + " : " + pos.getDest());

        if (GameRules.canMove(pos.getOrigin(), pos.getDest(), this.grid, this.isP1Turn)) {
            this.moveFigure(pos.getOrigin(), pos.getDest());
            if (this.isP1Turn) player2Input.readNBytes(2); else player1Input.readNBytes(2); // Falls der andere Client versucht ungültig zu bewegen, Stream leeren.
        }
        else
            System.out.println("Move was not valid!");

    }

    private void sendCommand(Socket player, PositionTuple pos) throws IOException {
        //new DataOutputStream(player.getOutputStream()).writeBytes(Arrays.toString(SerializingTools.serialize(pos).toArray(Byte[]::new)));
        List<Byte> positions = SerializingTools.serialize(pos).toList();
        player.getOutputStream().write(positions.get(0));
        player.getOutputStream().write(positions.get(1));
        player.getOutputStream().flush();

    }
    private void moveFigure(Position origin, Position dest) throws IOException {
        this.movedFromMarkerButton.setMovedAway(false);
        this.movedToMarkerButton.setMovedTo(false);
        ChessFieldButton originButton = this.grid[origin.getX()][origin.getY()];
        ChessFieldButton destButton = this.grid[dest.getX()][dest.getY()];
        ChessFigure toMove = this.grid[origin.getX()][origin.getY()].getFigureType();

        originButton.setFigure(ChessFigure.EMPTY);
        originButton.setMovedAway(true);

        destButton.setFigure(toMove);
        destButton.setTouched(true);
        destButton.setMovedTo(true);

        this.movedFromMarkerButton = originButton;
        this.movedToMarkerButton = destButton;

        this.isP1Turn = !this.isP1Turn;

        this.sendCommand(this.player1, new PositionTuple(origin, dest));
        this.sendCommand(this.player2, new PositionTuple(origin, dest));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        GameServerNew server = new GameServerNew();
        pool.execute(server::run);
        Thread.sleep(2000L);

        pool.execute(GameClient::runClient);
        pool.execute(GameClient::runClient);

        pool.close();
        pool.awaitTermination(1, TimeUnit.DAYS);
    }
}