package Model;

import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.ServerStrategyGenerateMaze;
import Server.*;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;
import Client.Client;
import sun.nio.cs.ext.MacArabic;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyModel extends Observable implements IModel {
    //private ExecutorService thread_pool = Executors.newCachedThreadPool();
    private Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private Server generateServer;
    private Server solveServer;

    public MyModel() {
        //Raise the servers
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        generateServer.start();
        solveServer.start();
    }

    public void stopServers() {
        generateServer.stop();
        solveServer.stop();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[1000012 /*assuming biggest maze is 1000x1000*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        //maze.print();
                        characterPositionRow = maze.getStartPosition().getRowIndex();
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            notifyObservers();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void moveCharacter(KeyCode direction) {
        switch (direction) {
            case NUMPAD1:
                characterPositionRow++;
                characterPositionColumn--;
                break;
            case NUMPAD2:
                characterPositionRow++;
                break;
            case NUMPAD3:
                characterPositionRow++;
                characterPositionColumn++;
                break;
            case NUMPAD4:
                characterPositionColumn--;
                break;
            case NUMPAD6:
                characterPositionColumn++;
                break;
            case NUMPAD7:
                characterPositionRow--;
                characterPositionColumn--;
                break;
            case NUMPAD8:
                characterPositionRow--;
                break;
            case NUMPAD9:
                characterPositionRow--;
                characterPositionColumn++;
                break;
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
}
