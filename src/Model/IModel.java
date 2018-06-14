package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

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
}
