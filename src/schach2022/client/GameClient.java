package schach2022.client;

import schach2022.communication.SerializingTools;
import schach2022.gameUtils.*;
import schach2022.utils.FailedToConnectException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameClient implements MouseListener {
    private final JFrame frame;
    private final ChessBoard board;
    private JPanel boardPanel;
    private ChessFieldButton movedFromMarkerButton;
    private ChessFieldButton movedToMarkerButton;
    private List<Position> markedPos;
    private ChessFieldButton previousClickedFigureButton;
    private Socket socket;
    private Thread receiveThread;
    private boolean isPlayer1;

    public GameClient() {
        this.frame = new JFrame("My Chess");
        this.frame.setSize(8 * ChessBoard.BUTTON_SIZE, 8 * ChessBoard.BUTTON_SIZE + 100);
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
        this.frame.setIconImage(new ImageIcon("C:/Users/yassi/IdeaProjects/Schach/src/schach2022/Icons/Icon.jpg").getImage());
        this.markedPos = new ArrayList<>();
        this.movedFromMarkerButton = new ChessFieldButton();
        this.movedToMarkerButton = new ChessFieldButton();

        this.board = ChessBoard.createClientField(this);
        this.boardPanel = new JPanel(new GridLayout(8, 8));

        for (ChessFieldButton[] arr : this.getGrid()) {
            for (ChessFieldButton button : arr) {
                this.boardPanel.add(button);
            }
        }
        this.frame.getContentPane().add(boardPanel);
        this.frame.pack();
        this.frame.setVisible(true);
        this.frame.repaint();
    }
    public void connectToServer() {
        try {
            this.socket = new Socket("localhost", 1337);
            this.isPlayer1 = (this.socket.getInputStream().read() == 1);
            if (!this.isPlayer1) {
                List<ChessFieldButton> temp = new ArrayList<>();
                for (ChessFieldButton[] chessFieldButtons : this.getGrid()) {
                    temp.addAll(Arrays.asList(chessFieldButtons));
                }
                this.boardPanel.removeAll();
                Collections.reverse(temp);
                for (ChessFieldButton b : temp) {
                    this.boardPanel.add(b);
                }
                this.frame.getContentPane().add(boardPanel);
                this.frame.pack();
                this.frame.setVisible(true);
                this.frame.repaint();
            }
            this.frame.repaint();
            this.receiveThread = new Thread(this::receiveCommand);
            this.receiveThread.start();
        }
        catch (IOException e) {
            throw new FailedToConnectException("Could not connect to Server. Please try again");
        }
    }

    private void sendRequest(PositionTuple pos) {
        try {
            //System.out.println(Arrays.toString(SerializingTools.serialize(pos).toArray(Byte[]::new)));
            List<Byte> x = SerializingTools.serialize(pos).toList();
            OutputStream out = this.socket.getOutputStream();
            out.write(x.get(0));
            out.write(x.get(1));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to send data");
        }
    }
    private void receiveCommand() {
        do {
            try {
                InputStream in = this.socket.getInputStream();
                if (in.available() > 0) {
                    PositionTuple toMovePos = SerializingTools.deSerialize(Arrays.stream(SerializingTools.box(in.readNBytes(2))));
                    this.board.moveFigure(toMovePos.getOrigin(), toMovePos.getDest());
                    this.frame.repaint();
                    //System.out.println("Client deSerialized: " + toMovePos.getOrigin() + " : " + toMovePos.getDest());
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to receive data");
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (this.receiveThread.isAlive());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ChessFieldButton buttonClicked = (ChessFieldButton) e.getSource();

        if (previousClickedFigureButton == null) {
            if (!GameRules.isEmpty(buttonClicked) && this.board.isMyPiece(buttonClicked, isPlayer1))
                previousClickedFigureButton = buttonClicked;
            else
                return;
        }
        if (!GameRules.isEmpty(buttonClicked)) {
            if (previousClickedFigureButton.figureType.color == buttonClicked.figureType.color) {
                if (previousClickedFigureButton.equals(buttonClicked)) {
                    if (buttonClicked.isSelected()) {
                        buttonClicked.setBackgroundToOrigin();
                        buttonClicked.setSelected(false);
                        previousClickedFigureButton = null;
                        this.board.markLegalMoves(Collections.emptyList());
                    }
                    else {
                        buttonClicked.setBackgroundToSelected();
                        buttonClicked.setSelected(true);
                        this.board.markLegalMoves(GameRules.getAvailablePos(buttonClicked.getPos(), this.board));
                    }
                }
                else {
                    previousClickedFigureButton.setBackgroundToOrigin();
                    previousClickedFigureButton.setSelected(false);
                    buttonClicked.setBackgroundToSelected();
                    buttonClicked.setSelected(true);
                    this.board.markLegalMoves(GameRules.getAvailablePos(buttonClicked.getPos(), this.board));
                }
                this.frame.repaint();
                previousClickedFigureButton = buttonClicked;
            }
            else {
                sendRequestAndClearBoard(buttonClicked);
            }
        }
        else {
            sendRequestAndClearBoard(buttonClicked);
        }
    }

    private void sendRequestAndClearBoard(ChessFieldButton buttonClicked) {
        this.sendRequest(new PositionTuple(previousClickedFigureButton.getPos(), buttonClicked.getPos()));

        previousClickedFigureButton.setBackgroundToOrigin();
        previousClickedFigureButton.setSelected(false);
        this.board.markLegalMoves(Collections.emptyList());
        buttonClicked.setBackgroundToOrigin();
        buttonClicked.setSelected(false);
        previousClickedFigureButton = null;
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

    public ChessFieldButton[][] getGrid() {
        return this.board.getGrid();
    }
    public static void runClient() {
        GameClient client = new GameClient();
        client.connectToServer();
    }
}
