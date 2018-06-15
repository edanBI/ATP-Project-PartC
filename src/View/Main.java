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
        primaryStage.setTitle("Miri-Run Maze Application");

        //create MVVM model, view model, view parts
        MyModel my_model = new MyModel();
        MyViewModel view_model = new MyViewModel(my_model);
        MyViewController view_controller;
        my_model.addObserver(view_model); /* view_model watching model */
        my_model.startServers();

        FXMLLoader fx_loader = new FXMLLoader();
        Parent root = fx_loader.load(getClass().getResource("MyView.fxml").openStream());
        Scene main_scene = new Scene(root, 600, 600);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        main_scene.getStylesheets().add(getClass().getResource("MyViewStyle.css").toExternalForm());
        primaryStage.show();

        view_controller = fx_loader.getController();
        view_controller.setViewModel(view_model);
        view_controller.setResizeEvent(main_scene);
        view_model.addObserver(view_controller);

        SetStageCloseEvent(primaryStage, my_model);
        primaryStage.setScene(main_scene);
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
