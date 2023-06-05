package vending.exceptions;

public class SoldOutException extends RuntimeException{
    private String message;
    public SoldOutException(String s) {
        this.message = s;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
