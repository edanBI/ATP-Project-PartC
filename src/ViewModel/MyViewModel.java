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
    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding
    public StringProperty goalPositionRow = new SimpleStringProperty("1");
    public StringProperty goalPositionColumn = new SimpleStringProperty("1");
    public StringProperty character_Position = new SimpleStringProperty(" ");

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if (arg.equals("maze generated")){
                characterPositionRowIndex = model.getCharacterPositionRow();
                characterPositionRow.set(characterPositionRowIndex + "");
                characterPositionColumnIndex = model.getCharacterPositionColumn();
                characterPositionColumn.set(characterPositionColumnIndex + "");
                goalPositionRowIndex = model.getGoalPositionRow();
                goalPositionRow.set(goalPositionRowIndex + "");
                goalPositionColumnIndex = model.getGoalPositionColumn();
                goalPositionColumn.set(goalPositionColumnIndex + "");
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
                characterPositionRow.set(characterPositionRowIndex + "");
                characterPositionColumnIndex = model.getCharacterPositionColumn();
                characterPositionColumn.set(characterPositionColumnIndex + "");
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
}
