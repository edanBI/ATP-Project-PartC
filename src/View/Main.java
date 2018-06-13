package View;

import Model.MyModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application{
    /*private Label l_rows, l_cols;
    private TextField tf_rows, tf_cols;
    private Button b_generate_maze, b_solve_maze;*/

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*MyModel model = new MyModel();
        model.startServers();
        //ViewModel viewModel = new ViewModel(model);
        MyViewController view = new MyViewController();
        model.addObserver(view);*/
        //--------------
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("MyMaze Application");
        /*Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        FXMLLoader fxmlLoader = new FXMLLoader();*/
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());

        /*l_rows = new Label("Number of rows");
        l_cols = new Label("Number of columns");
        tf_rows = new TextField();
        tf_cols = new TextField();
        b_generate_maze = new Button("Generate Maze");
        b_solve_maze = new Button("Get Solution");*/

        //--------------
        /*View view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);*/
        //--------------
        //SetStageCloseEvent(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    // Close program
                } else {
                    // ... user chose CANCEL or closed the dialog
                    windowEvent.consume();
                }
            }
        });
    }

    public static void main (String[] args) {
        launch(args);
    }
}
