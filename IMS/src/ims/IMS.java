package ims;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Skye McClure
 */
public class IMS extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management");

        try {
            FXMLLoader loader = new FXMLLoader(IMS.class.getResource("Inventory.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
