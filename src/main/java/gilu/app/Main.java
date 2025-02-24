package gilu.app;

import java.io.IOException;

import gilu.Gilu;
import gilu.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Gilu using FXML.
 */
public class Main extends Application {

    private static final String STORAGE_PATH = "./data/gilu.txt";
    private static final String FXML_PATH = "/view/MainWindow.fxml";

    private final Gilu gilu = new Gilu(STORAGE_PATH); // Ensure storage path is passed

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Inject Gilu instance into the UI controller
            MainWindow controller = fxmlLoader.getController();
            controller.setGilu(gilu);

            stage.setTitle("Gilu Chatbot");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage()); // Provide meaningful error feedback
            e.printStackTrace();
        }
    }
}
