package exception;

// User-Defined Checked Exception demonstrating custom exception handling
public class ShelterFullException extends Exception {

    // Default constructor
    public ShelterFullException() {
        super("Warning: Shelter is at maximum capacity. Cannot allocate more victims!");
    }

    // Parameterized constructor allowing custom messages
    public ShelterFullException(String message) {
        super(message);
    }
}
