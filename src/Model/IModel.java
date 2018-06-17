package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

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
    void stopServers();
    void loadMaze(File maze_file) throws FileNotFoundException;
    void updateServers();
    void updateSolution(Solution solution);
    void characterMouseDrag(MouseEvent mouseDragEvent);
    void stopMusic();
    void music();
    void mute(int i);
}
