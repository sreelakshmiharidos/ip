package gilu.exception;

/**
 * Represents exceptions specific to the Gilu chatbot.
 */
public class GiluException extends Exception {

    /**
     * Constructs a GiluException with the given message.
     *
     * @param message The error message.
     */
    public GiluException(String message) {
        super(message);
    }

    /**
     * Constructs a GiluException with a default error message.
     */
    public GiluException() {
        super("An unexpected error occurred in Gilu.");
    }
}