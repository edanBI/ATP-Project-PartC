package Model;

import Server.Server;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

import java.util.Observable;

public class MyModel extends Observable implements IModel {

    private Maze m_maze;
    private Server generator;

    public void startServers() {

    }

    @Override
    public void generateMaze(int row, int col) {
        m_maze = new Maze(row, col);
    }

    @Override
    public void moveCharacter(KeyCode movement) {

    }

    @Override
    public Maze getMaze() {
        return m_maze;
    }
}
