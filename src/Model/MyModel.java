package Model;
// been watched by MyViewModel
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.ServerStrategyGenerateMaze;
import Server.*;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;
import Client.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyModel extends Observable implements IModel {
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private Server generateServer;
    private Server solveServer;

    public MyModel() {
        //Raise the servers
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
    }

    public void startServers() {
    }

    public void stopServers() {
        generateServer.stop();
        solveServer.stop();
    }

    @Override
    public void generateMaze(int width, int height) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{50, 50};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[3000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        maze.print();
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

    //private int[][] generateRandomMaze(int width, int height) {
        //Random rand = new Random();
        //maze = new int[width][height];
        //for (int i = 0; i < maze.length; i++) {
            //for (int j = 0; j < maze[i].length; j++) {
            //    maze[i][j] = Math.abs(rand.nextInt() % 2);
          //  }
        //}
      //  return maze;
    //}

    @Override
    public int[][] getMaze() {
        return maze.getM_arr();
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case UP:
                characterPositionRow--;
                break;
            case DOWN:
                characterPositionRow++;
                break;
            case RIGHT:
                characterPositionColumn++;
                break;
            case LEFT:
                characterPositionColumn--;
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
