package View;
/*
Observe View Model
*/
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView {

    @FXML
    private MyViewModel view_model;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;

    private boolean isGenerated = false;

    public void setViewModel(MyViewModel my_viewModel) {
        this.view_model = my_viewModel;
        mazeDisplayer.requestFocus();
        bindProperties(my_viewModel);
    }

    /*
    * binds the text label in the main menu to the my_viewModel in the background
    * */
    private void bindProperties(MyViewModel viewModel) {
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRow); // display row pos
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn); // display col pos
    }

    public void update(Observable o, Object arg) {
        if (o == view_model) {
            if (arg.equals("maze generated")) {
                displayMaze(view_model.getMaze());
                btn_generateMaze.setDisable(false);
            }
            if (arg.equals("solution")) {
                mazeDisplayer.setSolution(view_model.getSolution());
                btn_solveMaze.setDisable(false);
            }
            if (arg.equals("moved")) {
                displayMaze(view_model.getMaze());
            }
        }
    }

    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze);
        int character_pos_row = view_model.getCharacterPositionRow();
        int character_pos_col = view_model.getCharacterPositionColumn();
        //int goalPositionRow = view_model.getGoalPositionRowIndex();
        //int goalPositionColumn = view_model.getGoalPositionColumnIndex();
        mazeDisplayer.setCharacterPosition(character_pos_row, character_pos_col); // display character on screen
        //mazeDisplayer.setGoalPosition(goalPositionRow, goalPositionColumn);
        this.characterPositionRow.set(character_pos_row + "");
        this.characterPositionColumn.set(character_pos_col + "");
        //my_viewModel.btn_solveMaze();
        //mazeDisplayer.setSolution(sol);
        //mazeDisplayer.requestFocus();
    }

    public void generateMaze() {
        int height = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        btn_generateMaze.setDisable(true);
        isGenerated = true;

        mazeDisplayer.setWidth(txtfld_rowsNum.getScene().getWidth() - 190);
        mazeDisplayer.setHeight(txtfld_rowsNum.getScene().getHeight() - 60);
        update(view_model, new Object());

        view_model.generateMaze(width, height);


    }

    public void solveMaze(ActionEvent actionEvent) {
        //showAlert("Solving maze..");
        btn_solveMaze.setDisable(true);
        if (isGenerated)
            view_model.solveMaze();
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    private void showFinishedGameAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        if (view_model.moveCharacter(keyEvent.getCode())){
            EndGameDialog.show();
        }
        keyEvent.consume();
    }

    public void goal_reached(){

    }

    //region String Property for Binding
    public StringProperty characterPositionRow = new SimpleStringProperty();

    public StringProperty characterPositionColumn = new SimpleStringProperty();

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                if (mazeDisplayer.getMaze() != null) {
                    mazeDisplayer.setWidth((double)newSceneWidth-190);
                    mazeDisplayer.redraw();
                }
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                if (mazeDisplayer.getMaze() != null) {
                    mazeDisplayer.setHeight((double)newSceneWidth-60);
                    mazeDisplayer.redraw();
                }
            }
        });
    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About - MyViewController");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }
}
