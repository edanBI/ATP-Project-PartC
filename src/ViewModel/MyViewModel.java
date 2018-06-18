package ViewModel;
/*
Observable by View
Observe Model
 */
import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private int goalPositionRowIndex;
    private int goalPositionColumnIndex;
    //private Position position;
    private Solution sol;
    public StringProperty characterPositionRow = new SimpleStringProperty(""); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty(""); //For Binding
    public StringProperty goalPositionRow = new SimpleStringProperty("");
    public StringProperty goalPositionColumn = new SimpleStringProperty("");
    public StringProperty character_Position = new SimpleStringProperty("");

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if (arg.equals("maze generated")){
                model.stopMusic();
                model.music("");

                characterPositionRowIndex = model.getCharacterPositionRow();
                characterPositionRow.set(characterPositionRowIndex + 1 + "");
                characterPositionColumnIndex = model.getCharacterPositionColumn();
                characterPositionColumn.set(characterPositionColumnIndex + 1 + "");
                goalPositionRowIndex = model.getGoalPositionRow();
                goalPositionRow.set(goalPositionRowIndex + 1 + "");
                goalPositionColumnIndex = model.getGoalPositionColumn();
                goalPositionColumn.set(goalPositionColumnIndex + 1 + "");
                character_Position.set(model.getMaze().getStartPosition().toString());
                setChanged();
                notifyObservers("maze generated");
            }
            if (arg.equals("maze solved") && model.getMaze() != null){
                sol = model.getSolution();
                setChanged();
                notifyObservers("solution");
            }
            if (arg.equals("moved")) {
                characterPositionRowIndex = model.getCharacterPositionRow();
                characterPositionRow.set(characterPositionRowIndex + 1 + "");
                characterPositionColumnIndex = model.getCharacterPositionColumn();
                characterPositionColumn.set(characterPositionColumnIndex + 1 + "");
                character_Position.set(getCharacter_Position().toString());
                setChanged();
                notifyObservers("player moved");
            }
        }
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public void solveMaze() {
        model.solveMaze();
    }

    public void generateMaze(int row, int col){
        model.generateMaze(row, col);
    }

    public boolean moveCharacter(KeyCode direction){
        return model.moveCharacter(direction);
    }

    public Solution getSolution() {
        return model.getSolution();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public int getGoalPositionRowIndex() {
        return goalPositionRowIndex;
    }

    public int getGoalPositionColumnIndex() {
        return goalPositionColumnIndex;
    }

    private Position getCharacter_Position() {
        return new Position(characterPositionRowIndex, characterPositionColumnIndex);
    }

    public void stopServer() {
        model.stopServers();
    }

    public void loadMaze(File maze_file) throws FileNotFoundException {
        model.loadMaze(maze_file);
    }

    public void updateServers() {
        model.updateServers();
    }

    public void updateSolution(Solution solution) {
        model.updateSolution(solution);
    }

    public void characterMouseDrag(int row_pos, int col_pos) {
        model.characterMouseDrag(row_pos, col_pos);
    }

    public void setSong(String url) {
        model.music(url);
    }

    public void mute (int i) {
        model.mute(i);
    }
}
