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

        //create MVVM model, view model, view parts
        MyModel my_model = new MyModel();
        my_model.startServers();
        MyViewModel view_model = new MyViewModel(my_model);
        my_model.addObserver(view_model); /* view_model watching model */


        primaryStage.setTitle("Miri Fun Run");
        FXMLLoader fx_loader = new FXMLLoader();
        MyViewController view_controller = fx_loader.getController();
        fx_loader.setRoot(this);
        fx_loader.setController(view_controller);
        //Parent root = fx_loader.load(getClass().getResource("MyView.fxml").openStream());
        Parent root = fx_loader.load(getClass().getResource("MyView.fxml").openStream());
        fx_loader.setClassLoader(getClass().getClassLoader());
        Scene main_scene = new Scene(root, 1200, 800);
        main_scene.getStylesheets().add(getClass().getResource("MyViewStyle.css").toExternalForm());
        primaryStage.setScene(main_scene);

        view_controller.setResizeEvent(main_scene);
        view_controller.setViewModel(view_model);
        view_model.addObserver(view_controller);

        SetStageCloseEvent(primaryStage, my_model);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage, MyModel model) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.stopServers();
                } else {
                    windowEvent.consume();
                }
            }
        });
    }

    public static void main (String[] args) {
        launch(args);
    }
}

