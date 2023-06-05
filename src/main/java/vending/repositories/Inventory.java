package vending.repositories;

import vending.exceptions.ItemDoesNotExistException;
import vending.exceptions.SoldOutException;

import java.util.HashMap;
import java.util.Map;

public class Inventory<T> {
    private Map<T, Integer> inventory = new HashMap<T, Integer>();

    // Returns the quantity of an item. If the item is not stocked, -1 is returned instead.
    public int getQuantity(T item) {
        Integer value = inventory.get(item);
        if (value == null) {
            throw new ItemDoesNotExistException("Item does not exist!");
        } else {
            return value;
        }
    }

    // Adds a new item to inventory or adds to existing stock
    public void add(T item) {
        if (inventory.get(item) != null && hasItem(item)) {
            int count = inventory.get(item);
            inventory.put(item, count+1);
        } else {
            inventory.put(item, 1);
        }
    }

    // Removes an item from the existing stock
    public void remove(T item) {
        if (hasItem(item) && inventory.get(item) > 0) {
            int count = inventory.get(item);
            inventory.put(item, count - 1);
        } else if (inventory.get(item) == 0) {
            throw new SoldOutException("Sold Out");
        }
    }

    // Checks if the item has more than 1 in stock
    public boolean hasItem(T item) {
        return getQuantity(item) > 0;
    }

    // Clears the inventory
    public void clear() {
        inventory.clear();
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    // Allows user update inventory with an item and its respective quantity
    public void put(T item, int quantity) {
        inventory.put(item, quantity);
    }

}
