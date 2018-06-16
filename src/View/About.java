package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.internal.dynalink.beans.StaticClass;

public  class About {
    public About() throws Exception {
        Stage window = new Stage();
        window.setTitle("Welcome");
        Parent layout = new FXMLLoader().load(getClass().getResource("About.fxml").openStream());
        Scene scene = new Scene(layout, 400, 500);
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        window.show();
    }
}
