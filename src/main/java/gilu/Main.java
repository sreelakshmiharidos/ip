package gilu;

import gilu.ui.MainWindow;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Gilu using FXML.
 */
public class Main extends Application {

    private Gilu gilu = new Gilu("./data/gilu.txt"); // Ensure storage path is passed

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Inject Gilu instance into the UI controller
            MainWindow controller = fxmlLoader.getController();
            controller.setGilu(gilu);

            stage.setTitle("Gilu Chatbot");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
