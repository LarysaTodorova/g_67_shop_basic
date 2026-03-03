package app.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(String.format("Product with ID %d not found", id));
    }
}
