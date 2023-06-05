package vending.entities;

public enum Money {
    NICKEL(5), DIME(10), QUARTER(25);

    private int value;

    private Money(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
