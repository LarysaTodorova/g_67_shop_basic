package app.exception;

public class CustomerUpdateException extends RuntimeException {
    public CustomerUpdateException(String message) {
        super(message);
    }
}
