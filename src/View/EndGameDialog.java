package View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EndGameDialog {

    public static void show() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(" ");
        window.setMinWidth(250);
        window.setMinHeight(150);
        window.setResizable(false);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(8);
        layout.setHgap(10);
        Scene scene = new Scene(layout);

        Button new_game = new Button("Restart Game");
        GridPane.setConstraints(new_game, 0, 1);
        Button end_game = new Button("End Game");
        GridPane.setConstraints(end_game, 1, 1);
        Label msg_label = new Label("Game Finished!");
        GridPane.setConstraints(msg_label, 0, 0);

        layout.getChildren().addAll(new_game, end_game, msg_label);
        window.setScene(scene);
        window.show();
    }
}
