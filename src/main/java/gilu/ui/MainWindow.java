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
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Gilu gilu;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image giluImage = new Image(this.getClass().getResourceAsStream("/images/DaGilu.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        // Show welcome message when the GUI loads
        dialogContainer.getChildren().add(
                DialogBox.getGiluDialog("Heyoo! I'm Gilu, your trusted friend!" +
                        "\nHow can I make your day better?", giluImage));
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
        String input = userInput.getText();
        String response = gilu.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getGiluDialog(response, giluImage)
        );
        userInput.clear();
    }
}
