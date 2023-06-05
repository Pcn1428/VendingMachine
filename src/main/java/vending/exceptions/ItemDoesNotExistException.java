package vending.exceptions;

public class ItemDoesNotExistException extends RuntimeException{
    private String message;
    public ItemDoesNotExistException(String s) {this.message = s;}
    @Override
    public String getMessage() {return message;}
}
