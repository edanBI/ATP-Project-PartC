package View;
/*
Observe View Model
*/
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
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
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class MyViewController implements Observer, IView{

    private MyViewModel view_model;
    private boolean prop_update;
    private Properties prop;
    private int sound;

    @FXML
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Label lbl_goalRowsNum;
    public javafx.scene.control.Label lbl_goalColumnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.Button btn_hideSolution;
    public javafx.scene.control.Button btn_mute;
    public javafx.scene.control.RadioMenuItem rmi_bfs;
    public javafx.scene.control.RadioMenuItem rmi_dfs;
    public javafx.scene.control.RadioMenuItem rmi_best;
    public javafx.scene.control.RadioMenuItem rmi_sGen;
    public javafx.scene.control.RadioMenuItem rmi_mGen;

    public MyViewController() {
        prop_update = false;
        sound = 0;
        prop = new Properties();
        OutputStream _out;
        InputStream _in;
        try {
            _in = new FileInputStream("resources/config.properties");
            prop.load(_in);
            _in.close();

            _out = new FileOutputStream("resources/config.properties");
            prop.setProperty("SolvingAlgorithm", "BestFirstSearch");
            prop.setProperty("generateAlgorithm", "MyMazeGenerator");

            prop.store(_out, null);
            _out.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

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
        lbl_goalRowsNum.textProperty().bind(viewModel.goalPositionRow);
        lbl_goalColumnsNum.textProperty().bind(viewModel.goalPositionColumn);
    }

    public void update(Observable o, Object arg) {
        if (o == view_model) {
            if (arg.equals("maze generated")) {
                displayMaze(view_model.getMaze());
                btn_generateMaze.setDisable(false);
                mazeDisplayer.requestFocus();
            }
            if (arg.equals("solution")) {
                mazeDisplayer.setSolution(view_model.getSolution());
                btn_solveMaze.setDisable(false);
                mazeDisplayer.requestFocus();
            }
            if (arg.equals("player moved")) {
                mazeDisplayer.requestFocus();
                int positionRow = view_model.getCharacterPositionRow();
                int positionColumn = view_model.getCharacterPositionColumn();
                mazeDisplayer.setCharacterPosition(positionRow, positionColumn); // display character on screen
                this.characterPositionRow.set(positionRow + "");
                this.characterPositionColumn.set(positionColumn + "");

                String curr_pos = "{" + positionRow + "," + positionColumn + "}";
                if (mazeDisplayer.wantSolution && view_model.getSolution() != null) {
                    ArrayList<AState> solution_arr = view_model.getSolution().getSolutionPath();
                    if (solution_arr.get(1).toString().equals(curr_pos)) {
                        solution_arr.remove(0);
                        Solution new_sol = new Solution(solution_arr);
                        view_model.updateSolution(new_sol);
                        mazeDisplayer.setSolution(new_sol);
                        mazeDisplayer.redraw();
                    }
                }

                if (view_model.getGoalPositionRowIndex()==positionRow && view_model.getGoalPositionColumnIndex()==positionColumn){
                    //EndGameDialog.show();
                    System.out.println("GOAL!");
                    try {
                        Stage goal_window = new Stage();
                        goal_window.setTitle("HAHAHAHAHA");
                        goal_window.setResizable(false);
                        Parent layout = new FXMLLoader().load(getClass().getResource("GoalWindow.fxml").openStream());
                        Scene scene = new Scene(layout, 520, 290);
                        goal_window.setScene(scene);
                        goal_window.initModality(Modality.APPLICATION_MODAL);

                        view_model.setSong("./resources/Images/lionel_messi_lololo.mp3");
                        goal_window.showAndWait();


                    } catch (Exception e) {
                        System.out.println(e.getCause());
                    }
                }
            }
        }
    }
    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze);
        int character_pos_row = view_model.getCharacterPositionRow();
        int character_pos_col = view_model.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(character_pos_row, character_pos_col); // display character on screen
        this.characterPositionRow.set(character_pos_row + "");
        this.characterPositionColumn.set(character_pos_col + "");

        mazeDisplayer.requestFocus();
        btn_solveMaze.setDisable(false);
    }

    public void generateMaze() {
        try {
            int row_in = Integer.valueOf(txtfld_rowsNum.getText());
            int col_in = Integer.valueOf(txtfld_columnsNum.getText());
            if (row_in < 4) row_in = 4;
            if (col_in < 4) col_in = 4;

            btn_generateMaze.setDisable(true);
            mazeDisplayer.setWidth(mazeDisplayer.getScene().getWidth() - 195);
            mazeDisplayer.setHeight(mazeDisplayer.getScene().getHeight() - 49);
            btn_mute.setDisable(false);
            view_model.generateMaze(row_in, col_in);
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong maze size input. " +
                    "Try again.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    public void solveMaze(ActionEvent actionEvent) {
        btn_hideSolution.setDisable(false);
        if (prop_update) {
            if (view_model.getMaze() != null) {
                btn_solveMaze.setDisable(true);
                view_model.solveMaze();
            }
        }
        else {
            if (view_model.getMaze() != null) {
                btn_solveMaze.setDisable(true);
                view_model.solveMaze();
            }
        }

        prop_update = false;
    }

    public void hideSolution() {
        mazeDisplayer.wantSolution = false;
        displayMaze(view_model.getMaze());
        btn_hideSolution.setDisable(true);
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

    public void mouseDrag(MouseEvent mouse_event) {
        mazeDisplayer.requestFocus();
        int c_row = Integer.valueOf(txtfld_rowsNum.getText());
        int c_col = Integer.valueOf(txtfld_columnsNum.getText());
        double cc = mazeDisplayer.getParent().getBoundsInParent().getMinX();
        System.out.println(cc);
        int mouse_x = (int)(mouse_event.getX()-178);
        int mouse_y = (int)(mouse_event.getY()-32);

        if (mouse_event.isPrimaryButtonDown()) {
            view_model.characterMouseDrag((int)((mouse_y / mazeDisplayer.getHeight())*c_row), (int)((mouse_x / mazeDisplayer.getWidth())*c_col));
        }
    }

    public void mouseScroll(ScrollEvent scrollEvent) {
        if (scrollEvent.isControlDown()) {
            if (scrollEvent.getDeltaY() > 0) { //for zooming in
                mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()*1.01);
                mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()*1.01);
            }
            else { //for zooming out
                mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()/1.01);
                mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()/1.01);
            }
        }
    }

    public void KeyPressed(KeyEvent keyEvent) {
        view_model.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                if (mazeDisplayer.getMaze() != null) {
                    mazeDisplayer.setWidth((double) newSceneWidth.intValue() - 195);
                    if (view_model.getSolution() != null)
                        mazeDisplayer.setWantSolution(true);
                }
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                if (mazeDisplayer.getMaze() != null) {
                    mazeDisplayer.setHeight((double) (newSceneHeight.intValue() - 49));
                    if (view_model.getSolution() != null)
                        mazeDisplayer.setWantSolution(true);
                }
            }
        });
    }

    public void properties() {
        OutputStream _out;
        InputStream _in;
        try {
            _in = new FileInputStream("resources/config.properties");
            prop.load(_in);
            _in.close();

            _out = new FileOutputStream("resources/config.properties");
            if (rmi_bfs.isSelected()) {
                prop.setProperty("SolvingAlgorithm", "BreadthFirstSearch");
            } else if (rmi_dfs.isSelected()) {
                prop.setProperty("SolvingAlgorithm", "DepthFirstSearch");
            } else if (rmi_best.isSelected()) {
                prop.setProperty("SolvingAlgorithm", "BestFirstSearch");
            }

            if (rmi_sGen.isSelected()) {
                prop.setProperty("generateAlgorithm", "SimpleMazeGenerator");
            } else if (rmi_mGen.isSelected()) {
                prop.setProperty("generateAlgorithm", "MyMazeGenerator");
            }

            prop.store(_out, null);
            _out.close();
            view_model.updateServers();
            prop_update = true;
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void saveMaze() {
        if (view_model.getMaze() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to generate maze first..");
            alert.showAndWait();
            return;
        }
        String num_rows = Integer.valueOf(txtfld_rowsNum.getText()).toString();
        String num_cols = Integer.valueOf(txtfld_columnsNum.getText()).toString();

        FileChooser dialog = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("maze file (*.maze)", "*.maze");
        dialog.setTitle("Save Maze");
        dialog.setInitialFileName("Maze "+num_rows+"x"+num_cols + ".maze");
        dialog.getExtensionFilters().add(filter);
        File file = dialog.showSaveDialog(new Stage());
        if (file != null) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(view_model.getMaze().toByteArray());
                out.close();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void loadMaze() {
        try {
            mazeDisplayer.setWidth(mazeDisplayer.getScene().getWidth() - 195);
            mazeDisplayer.setHeight(mazeDisplayer.getScene().getHeight() - 49);
            btn_mute.setDisable(false);
            FileChooser dialog = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("All maze files (*.maze)", "*.maze");
            dialog.setTitle("Load Maze");
            dialog.getExtensionFilters().add(filter);
            File file = dialog.showOpenDialog(new Stage());
            if (file == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Select .maze file");
                alert.showAndWait();
                return;
            }
            view_model.loadMaze(file);
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public void About() {
        try {
            Stage window = new Stage();
            window.setTitle("Welcome");
            Parent layout = new FXMLLoader().load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(layout, 600, 232);
            window.setScene(scene);
            window.initModality(Modality.APPLICATION_MODAL);
            window.showAndWait();
        } catch (Exception e) {e.getStackTrace(); }
    }

    public void Help() {
        try {
            Stage window = new Stage();
            window.setTitle("Help");
            Parent layout = new FXMLLoader().load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(layout, 600, 232);
            window.setScene(scene);
            //window.initModality(Modality.APPLICATION_MODAL);
            //window.showAndWait();
            window.show();
        } catch (Exception e) {e.getStackTrace(); }
    }

    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            view_model.stopServer();
            System.exit(0);
        }
    }

    public void mute() {
        view_model.mute(sound);
        sound++;
    }

    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();
    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }
    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }
    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }
    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }
}
