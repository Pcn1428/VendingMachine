package vending.exceptions;

public class InsufficientFundsException extends RuntimeException {
    private String message;
    private long remaining;

    public InsufficientFundsException(String message, long remaining) {
        this.message = message;
        this.remaining = remaining;
    }

    public long getRemaining() {
        return remaining;
    }

    @Override
    public String getMessage() {
        return message + remaining;
    }
}
