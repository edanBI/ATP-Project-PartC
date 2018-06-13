package Model;

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


public class MyModel extends Observable implements IModel {
    //private ExecutorService thread_pool = Executors.newCachedThreadPool();
    private Maze m_maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private Server generateServer;
    private Server solveServer;
    private int goalPositionRow;
    private int goalPositionColumn;

    public MyModel() {
        //Raise the servers
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        generateServer.start();
        System.out.println("Generate Server is Running");
        solveServer.start();
        System.out.println("Solve Server is Running");
    }

    public void stopServers() {
        generateServer.stop();
        System.out.println("Generate Server has Stopped");
        solveServer.stop();
        System.out.println("Solve Server has Stopped");
    }

    @Override
    public void generateMaze(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send m_maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated m_maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[1000012 /*assuming biggest m_maze is 1000x1000*/]; //allocating byte[] for the decompressed m_maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        m_maze = new Maze(decompressedMaze);
                        m_maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        characterPositionRow = m_maze.getStartPosition().getRowIndex();
        characterPositionColumn = m_maze.getStartPosition().getColumnIndex();
        goalPositionRow = m_maze.getGoalPosition().getRowIndex();
        goalPositionColumn = m_maze.getGoalPosition().getColumnIndex();
        setChanged();
        notifyObservers();
    }

    @Override
    public Maze getMaze() {
        return m_maze;
    }

    @Override
    public void moveCharacter(KeyCode direction) {
        switch (direction) {
            case DOWN:
                if (legal_move(getCharacterPositionRow()+1, getCharacterPositionColumn())) {
                    characterPositionRow++;
                    break;
                }
                else
                    break;
            case LEFT:
                if (legal_move(getCharacterPositionRow(), getCharacterPositionColumn()-1)) {
                    characterPositionColumn--;
                    break;
                }
                else
                    break;
            case RIGHT:
                if (legal_move(getCharacterPositionRow(), getCharacterPositionColumn()+1)) {
                    characterPositionColumn++;
                    break;
                }
                else
                    break;
            case UP:
                if (legal_move(getCharacterPositionRow()-1, getCharacterPositionColumn())) {
                    characterPositionRow--;
                    break;
                }
                else
                    break;

            /*case NUMPAD1:
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
                break;*/
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

    private boolean legal_move(int row, int col){
        return row>=0 && row<m_maze.getM_arr().length && col>=0 && col<m_maze.getM_arr()[0].length
                && m_maze.getM_arr()[row][col]==0;
    }

    public int getGoalPositionColumn() {
        return goalPositionColumn;
    }

    public int getGoalPositionRow() {
        return goalPositionRow;
    }
}
