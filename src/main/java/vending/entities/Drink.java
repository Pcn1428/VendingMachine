package vending.entities;

public enum Drink {
    COLA ("COLA", 25), DIET_COLA ("DIET_COLA", 35), LIME_SODA("LIME_SODA", 25), WATER("WATER", 45), TEST("TEST", 99);

    private String name;
    private int price;

    private Drink(String name, int price) {
        this. name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
