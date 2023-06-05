package vending.exceptions;

public class InsufficientChangeException extends RuntimeException {
    private String message;

    public InsufficientChangeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
