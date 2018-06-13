package View;

import Model.MyModel;
import ViewModel.MyViewModel;
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GOT Maze Application");

        MyModel model = new MyModel();
        MyViewModel view_model = new MyViewModel(model);
        MyViewController view_controller = new MyViewController();

        model.startServers();
        model.addObserver(view_model); /* view_model watching model */

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());

        Scene scene = new Scene(root, 500, 300);
        scene.getStylesheets().add(getClass().getResource("MyViewStyle.css").toExternalForm());

        view_controller = fxmlLoader.getController();
        view_controller.setResizeEvent(scene);
        view_controller.setViewModel(view_model);
        view_model.addObserver(view_controller);

        SetStageCloseEvent(primaryStage, model);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage, MyModel model) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.stopServers();
                    // ... user chose OK
                    // Stop servers
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
