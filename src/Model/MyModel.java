package Model;
 /*
 Observable by View Model
 */
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.ServerStrategyGenerateMaze;
import Server.*;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import Client.Client;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;


public class MyModel extends Observable implements IModel {
    //private ExecutorService thread_pool = Executors.newCachedThreadPool();
    private Maze m_maze;
    private Solution mazeSolution;
    private int characterPositionRow;
    private int characterPositionColumn;
    private Server generateServer;
    private Server solveServer;
    private int goalPositionRow;
    private int goalPositionColumn;
    private MediaPlayer MP;

    public MyModel() {
        //Raise the servers
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        Media media = new Media(new File("./resources/Images/miri_song.mp3").toURI().toString());
        MP =new MediaPlayer(media);
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

    public void updateServers() {
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
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
                        //m_maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mazeSolution = null;
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        characterPositionRow = m_maze.getStartPosition().getRowIndex();
        characterPositionColumn = m_maze.getStartPosition().getColumnIndex();
        goalPositionRow = m_maze.getGoalPosition().getRowIndex();
        goalPositionColumn = m_maze.getGoalPosition().getColumnIndex();
        setChanged();
        notifyObservers("maze generated");
    }

    @Override
    public void loadMaze(File maze_file) throws FileNotFoundException {
        try {
            FileInputStream in = new FileInputStream(maze_file);
            byte[] tmp = new byte[1000024];
            in.read(tmp);
            m_maze = new Maze(tmp);
        } catch (IOException e) {e.printStackTrace();}
        characterPositionRow = m_maze.getStartPosition().getRowIndex();
        characterPositionColumn = m_maze.getStartPosition().getColumnIndex();
        goalPositionRow = m_maze.getGoalPosition().getRowIndex();
        goalPositionColumn = m_maze.getGoalPosition().getColumnIndex();
        setChanged();
        notifyObservers("maze generated");
    }

    @Override
    public void solveMaze() {
        try {
            mazeSolution = null;
            if (m_maze == null)
                return;
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        //MyMazeGenerator mg = new MyMazeGenerator();
                        //Maze maze = mg.generate(50, 50);
                        //maze.print();
                        m_maze.setsPos(new Position(characterPositionRow,characterPositionColumn));
                        toServer.writeObject(m_maze); //send maze to server
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        //print Maze Solution retrieved from the server
                        //System.out.println(String.format("Solution steps: %s", mazeSolution));
                        //ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        //for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                        //  System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Maze getMaze() {
        return m_maze;
    }

    @Override
    public Solution getSolution() {
        return mazeSolution;
    }

    @Override
    public boolean moveCharacter(KeyCode direction) {
        switch (direction) {
            case DOWN:
                if (legal_move(characterPositionRow+1, characterPositionColumn))
                    characterPositionRow++;
                break;
            case LEFT:
                if (legal_move(characterPositionRow, characterPositionColumn-1))
                    characterPositionColumn--;
                break;
            case RIGHT:
                if (legal_move(characterPositionRow, characterPositionColumn+1))
                    characterPositionColumn++;
                break;
            case UP:
                if (legal_move(characterPositionRow-1, characterPositionColumn))
                    characterPositionRow--;
                break;
            case NUMPAD1:
                if ( (legal_move(characterPositionRow+1, characterPositionColumn) ||
                        legal_move(characterPositionRow, characterPositionColumn-1))
                        && legal_move(characterPositionRow+1, characterPositionColumn-1)){
                    characterPositionRow++;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD2:
                if (legal_move(characterPositionRow+1, characterPositionColumn)){
                    characterPositionRow++;
                }
                break;
            case NUMPAD3:
                if ( (legal_move(characterPositionRow+1, characterPositionColumn) ||
                        legal_move(characterPositionRow, characterPositionColumn+1))
                        && legal_move(characterPositionRow+1, characterPositionColumn+1)){
                    characterPositionRow++;
                    characterPositionColumn++;
                }
                break;
            case NUMPAD4:
                if (legal_move(characterPositionRow, characterPositionColumn-1)){
                    characterPositionColumn--;
                }
                break;
            case NUMPAD6:
                if (legal_move(characterPositionRow, characterPositionColumn+1)){
                    characterPositionColumn++;
                }
                break;
            case NUMPAD7:
                if ( (legal_move(characterPositionRow, characterPositionColumn-1) ||
                        legal_move(characterPositionRow-1, characterPositionColumn))
                        && legal_move(characterPositionRow-1, characterPositionColumn-1)){
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD8:
                if (legal_move(characterPositionRow-1, characterPositionColumn)){
                    characterPositionRow--;
                }
                break;
            case NUMPAD9:
                if ( (legal_move(characterPositionRow, characterPositionColumn+1)
                        || legal_move(characterPositionRow-1, characterPositionColumn))
                        && legal_move(characterPositionRow-1, characterPositionColumn+1)){
                    characterPositionRow--;
                    characterPositionColumn++;
                }
                break;
        }
        setChanged();
        notifyObservers("moved");

        return getCharacterPositionRow()==getGoalPositionRow()
                && getCharacterPositionColumn()==getGoalPositionColumn();
    }

    private boolean legal_move(int row, int col){
        return row>=0 && row<m_maze.getM_arr().length && col>=0 && col<m_maze.getM_arr()[0].length
                && m_maze.getM_arr()[row][col]==0;
    }
    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public int getGoalPositionColumn() {
        return goalPositionColumn;
    }

    public int getGoalPositionRow() {
        return goalPositionRow;
    }

    @Override
    public void updateSolution(Solution solution) {
        this.mazeSolution = solution;
    }

    @Override
    public void characterMouseDrag(int row, int col) {
        if (row == characterPositionRow && col == characterPositionColumn)
            return;
        if (row==characterPositionRow+1 && col==characterPositionColumn-1) moveCharacter(KeyCode.NUMPAD1);
        else if (row==characterPositionRow+1 && col==characterPositionColumn) moveCharacter(KeyCode.NUMPAD2);
        else if (row==characterPositionRow+1 && col==characterPositionColumn+1) moveCharacter(KeyCode.NUMPAD3);
        else if (row==characterPositionRow && col==characterPositionColumn-1) moveCharacter(KeyCode.NUMPAD4);
        else if (row==characterPositionRow && col==characterPositionColumn+1) moveCharacter(KeyCode.NUMPAD6);
        else if (row==characterPositionRow-1 && col==characterPositionColumn-1) moveCharacter(KeyCode.NUMPAD7);
        else if (row==characterPositionRow-1 && col==characterPositionColumn) moveCharacter(KeyCode.NUMPAD8);
        else if (row==characterPositionRow-1 && col==characterPositionColumn+1) moveCharacter(KeyCode.NUMPAD9);
    }

    @Override
    public void mute(int i) {
        if (i % 2 != 0)
            MP.setMute(false);
        else
            MP.setMute(true);
    }
    @Override
    public void music(){
        MP.setVolume(0.3);
        MP.play();
    }
    @Override
    public void stopMusic() {
        MP.stop();
    }
}
