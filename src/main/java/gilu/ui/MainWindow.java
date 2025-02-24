package gilu.ui;

import gilu.Gilu;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    private static final String WELCOME_MESSAGE = "Heyoo! I'm Gilu, your trusted task-manager!"
            + "\nHow can I make your day better?";

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Gilu gilu;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image giluImage = new Image(getClass().getResourceAsStream("/images/DaGilu.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Show welcome message when the GUI loads
        dialogContainer.getChildren().add(
                DialogBox.getGiluDialog(WELCOME_MESSAGE, giluImage));
    }

    /**
     * Injects the Gilu instance.
     */
    public void setGilu(Gilu gilu) {
        this.gilu = gilu;
    }

    /**
     * Handles user input and generates Gilu's response.
     */
    @FXML
    private void handleUserInput() {
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userInput.getText(), userImage),
                DialogBox.getGiluDialog(gilu.getResponse(userInput.getText()), giluImage)
        );
        userInput.clear();
    }
}
