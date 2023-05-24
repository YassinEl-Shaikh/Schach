package schach2022.server;

import schach2022.client.GameClient;
import schach2022.gameUtils.*;
import schach2022.communication.SerializingTools;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServerNew {
    private final ChessBoard board;

    private Stack<PositionTuple> moves;
    private boolean isP1Turn;
    private Socket player1;
    private Socket player2;
    private final ServerSocket socket;

    public GameServerNew() {
        try {
            this.socket = new ServerSocket(1337);
        } catch (IOException e) {
            throw new RuntimeException("Was not able to set port: 1337");
        }
        this.isP1Turn = true;
        this.moves = new Stack<>();
        this.board = ChessBoard.generateServerField();
    }

    private void connectPlayers() {
        this.player1 = connectPlayer(1, (byte) 1);
        this.player2 = connectPlayer(2, (byte) 2);
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
        boolean running = true;
        System.out.println("Players connected!");
        do {
            try {
                this.getAndExecutePlayerMoves();
            } catch (IOException e) {
                throw new RuntimeException(e + "MEINE RUNTIME");
            }

            if (GameRules.isCheckMate(GameRules.getKing(this.board.getGrid(), this.isP1Turn ? ChessFigure.KING_BLACK.color : ChessFigure.KING_WHITE.color), this.board.getGrid())
                    || GameRules.isStaleMate())
                running = false;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while(running);
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
        //System.out.println("Server deserialized: " + pos.getOrigin() + " : " + pos.getDest());

        if (GameRules.canMove(pos.getOrigin(), pos.getDest(), this.board, this.isP1Turn)) {
            this.board.moveFigure(pos.getOrigin(), pos.getDest());
            this.moves.push(pos);
            if (this.isP1Turn) player2Input.skip(player2Input.available()); else player1Input.skip(player1Input.available()); // Falls der andere Client versucht ungültig zu bewegen, Stream leeren.
            this.isP1Turn = !this.isP1Turn;
            this.sendCommand(this.player1, pos);
            this.sendCommand(this.player2, pos);
        }
        else
            System.out.println("Move was not valid!");

    }

    private void sendCommand(Socket player, PositionTuple pos) throws IOException {
        //new DataOutputStream(player.getOutputStream()).writeBytes(Arrays.toString(SerializingTools.serialize(pos).toArray(Byte[]::new)));
        List<Byte> positions = SerializingTools.serialize(pos).toList();
        OutputStream out = player.getOutputStream();
        out.write(positions.get(0));
        out.write(positions.get(1));
        out.flush();
    }

    public static void main(String[] args) throws InterruptedException {
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