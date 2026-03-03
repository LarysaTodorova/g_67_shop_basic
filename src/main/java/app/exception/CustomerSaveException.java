package app.exception;

public class CustomerSaveException extends RuntimeException {
    public CustomerSaveException(String message) {
        super(message);
    }
}
