package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.FileNotFoundException;

public interface IModel {
    void generateMaze(int row, int col);
    void solveMaze ();
    boolean moveCharacter(KeyCode direction);
    Maze getMaze();
    Solution getSolution();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    int getGoalPositionColumn();
    int getGoalPositionRow();
    void startServers();
    void stopServers();
    void loadMaze(File maze_file) throws FileNotFoundException;
    void updateServers();
    void updateSolution(Solution solution);
    void characterMouseDrag(int row_pos, int row_col);
    void stopMusic();
    void music(String url);
    void mute(int i);
}
