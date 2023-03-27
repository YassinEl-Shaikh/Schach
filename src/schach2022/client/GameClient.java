package schach2022.client;

import schach2022.communication.SerializingTools;
import schach2022.gameUtils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GameClient implements MouseListener {
    private final JFrame frame;
    private final ChessFieldButton[][] grid;
    private ChessFieldButton movedFromMarkerButton;
    private ChessFieldButton movedToMarkerButton;
    private List<Position> markedPos;
    private PositionTuple toMovePos;
    private ChessFieldButton previousClickedFigureButton;
    private Socket socket;

    private Thread receiveThread;

    private boolean isPlayer1;
    private JLabel turnAssign;

    public GameClient() {
        this.frame = new JFrame("My Chess");
        this.frame.setSize(850, 850);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setLocation(500, 100);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Thread.currentThread().interrupt();
                receiveThread.interrupt();
            }
        });
        this.markedPos = new ArrayList<>();
        this.movedFromMarkerButton = new ChessFieldButton(null, null, null);
        this.movedToMarkerButton = new ChessFieldButton(null, null, null);

        this.grid = FieldInitialization.initiateField(this);
        for (ChessFieldButton[] arr : this.grid) {
            for (ChessFieldButton button : arr) {
                this.frame.add(button);
            }
        }
        this.frame.repaint();
    }

    public void connectToServer() {
        try {
            this.socket = new Socket("localhost", 1337);
            this.isPlayer1 = (this.socket.getInputStream().read() == 1);
            if (!this.isPlayer1) {
                for (ChessFieldButton[] ch : this.grid) {
                    for (ChessFieldButton c : ch) {
                        c.setBounds(c.getX(), 700 -c.getY(), 100, 100);
                    }
                }
            }
            //this.frame.setVisible(true);
            this.frame.repaint();
            this.receivePlayerNumber();
            this.receiveThread = new Thread(this::receiveCommand);
            this.receiveThread.start();
        }
        catch (IOException e) {
            throw new RuntimeException("Could not connect to Server. Please try again");
        }
    }

    private void sendRequest(PositionTuple pos) {
        try {
            //System.out.println(Arrays.toString(SerializingTools.serialize(pos).toArray(Byte[]::new)));
            List<Byte> x = SerializingTools.serialize(pos).toList();
            this.socket.getOutputStream().write(x.get(0));
            this.socket.getOutputStream().write(x.get(1));
            this.socket.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to send data");
        }
    }

    private void receivePlayerNumber() {
        try {
            this.isPlayer1 = this.socket.getInputStream().read() == 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void receiveCommand() {
        do {
            try {
                if (this.socket.getInputStream().available() > 0) {
                    this.toMovePos = SerializingTools.deSerialize(Arrays.stream(SerializingTools.box(this.socket.getInputStream().readNBytes(2))));
                    System.out.println("Client deSerialized: " + this.toMovePos.getOrigin() + " : " + this.toMovePos.getDest());
                }
                this.moveFigure(this.toMovePos.getOrigin(), this.toMovePos.getDest());
            } catch (IOException e) {
                throw new RuntimeException("Failed to receive data");
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (this.receiveThread.isAlive());
    }

    private void moveFigure(Position origin, Position dest) {
        if (this.toMovePos == null)
            return;
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

        destButton.setFigure(toMove);
        destButton.setTouched(true);
        destButton.setBackground(Color.PINK);
        destButton.setMovedTo(true);

        this.movedFromMarkerButton = originButton;
        this.movedToMarkerButton = destButton;

        this.removeMarker();
        this.previousClickedFigureButton = null;
        this.toMovePos = null;

        this.frame.repaint();
    }

    private void removeMarker() {
        for (Position p: this.markedPos) {
            if (this.grid[p.getX()][p.getY()].wasMovedTo()) {
                this.grid[p.getX()][p.getY()].setBackground(Color.PINK);
                continue;
            }
            else
                this.grid[p.getX()][p.getY()].setBackground(Color.WHITE);

            if (this.grid[p.getX()][p.getY()].wasMovedAway())
                this.grid[p.getX()][p.getY()].setBackground(Color.GREEN);
            else
                this.grid[p.getX()][p.getY()].setBackground(Color.WHITE);
        }
    }

    private void markAvailablePos(ChessFieldButton button) {
        List<Position> positions = GameRules.getAvailablePos(button.getPos(), this.grid);

        this.removeMarker();

        if (positions.size() == 0)
            return;
        for (Position p : positions) {
            this.grid[p.getX()][p.getY()].setBackground(Color.GRAY);
        }
        this.markedPos = new ArrayList<>(positions);
        this.frame.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ChessFieldButton buttonClicked = (ChessFieldButton) e.getSource();

        if (previousClickedFigureButton == null)
            previousClickedFigureButton = buttonClicked;

        if (buttonClicked.getBackground() == Color.blue) {
            buttonClicked.setBackground(Color.WHITE);
            this.removeMarker();
        }
        else {
            if (!GameRules.isEmpty(buttonClicked)) {
                if (previousClickedFigureButton.figureType.color == buttonClicked.figureType.color) {
                    previousClickedFigureButton.setBackground(Color.WHITE);
                    buttonClicked.setBackground(Color.BLUE);
                    this.markAvailablePos(buttonClicked);
                    previousClickedFigureButton = buttonClicked;
                }
                else {
                    if (GameRules.canMove(previousClickedFigureButton.getPos(), buttonClicked.getPos(), this.grid, previousClickedFigureButton.figureType.color == Color.WHITE)) {
                        this.sendRequest(new PositionTuple(previousClickedFigureButton.getPos(), buttonClicked.getPos()));
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Invalid field!", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                        /*
                        removeMarker();
                        previousClickedFigureButton.setBackground(Color.WHITE);
                        previousClickedFigureButton = null;

                         */
                    }
                }
            }
            else {
                if (GameRules.canMove(previousClickedFigureButton.getPos(), buttonClicked.getPos(), this.grid,
                        previousClickedFigureButton.figureType.color == Color.WHITE)) {
                    this.sendRequest(new PositionTuple(previousClickedFigureButton.getPos(), buttonClicked.getPos()));
                }
                else {
                    JOptionPane.showMessageDialog(null, "Invalid field!", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                        /*
                        removeMarker();
                        previousClickedFigureButton.setBackground(Color.WHITE);
                        previousClickedFigureButton = null;

                         */
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static void runClient() {
        GameClient client = new GameClient();
        client.connectToServer();
    }
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(GameClient::runClient);
        pool.execute(GameClient::runClient);
        pool.close();
        pool.awaitTermination(1, TimeUnit.DAYS);
       //  && (isEmpty(field[oX + 1][oY]) || isEmpty(field[oX - 1][oY])

/*
        game.moveFigure(new Position(6,0), new Position(4, 0));
        game.moveIfWanted();
        game.turn++;

 */
        //game.markAvailablePos(game.grid[7][0]);
        //System.out.println(game.grid[0][7].figureType);
        //System.out.println(game);

        //System.out.println(game.getGrid());
        //game.printGrid();
    }
}
