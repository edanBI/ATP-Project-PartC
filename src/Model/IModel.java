package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

public interface IModel {
    void generateMaze(int row, int col);
    void moveCharacter(KeyCode direction);
    Maze getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
}
