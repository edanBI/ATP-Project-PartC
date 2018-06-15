package ViewModel;
/*
Observable by View
Observe Model
 */
import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private int goalPositionRowIndex;
    private int goalPositionColumnIndex;
    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding
    public StringProperty goalPositionRow = new SimpleStringProperty("1");
    public StringProperty goalPositionColumn = new SimpleStringProperty("1");

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            goalPositionRowIndex = model.getGoalPositionRow();
            goalPositionRow.set(goalPositionRowIndex + "");
            goalPositionColumnIndex = model.getGoalPositionColumn();
            goalPositionColumn.set(goalPositionColumnIndex + "");
            setChanged();
            notifyObservers();
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

    public Solution getSolution(){return model.getSolution();}

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
}
