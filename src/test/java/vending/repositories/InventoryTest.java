package vending.repositories;

import org.junit.jupiter.api.Test;
import vending.entities.Drink;
import vending.entities.Money;
import vending.exceptions.ItemDoesNotExistException;
import vending.exceptions.SoldOutException;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    // Helper method to generate a known inventory
    public static Inventory generateTestInventory() {
        Inventory<Object> inventory = new Inventory<>();

        inventory.put(Money.NICKEL,5);
        inventory.put(Money.DIME,5);
        inventory.put(Money.QUARTER, 5);

        inventory.put(Drink.COLA,10);
        inventory.put(Drink.DIET_COLA,8);
        inventory.put(Drink.LIME_SODA,0);
        inventory.put(Drink.WATER,2);

        return inventory;
    }

    // Test Case 1: If the item exists, return the correct value
    @Test
    void testGetQuantity1() {
        Inventory<Object> i = generateTestInventory();
        assertEquals(0, i.getQuantity(Drink.LIME_SODA));
    }

    // Test Case 2: If the item does not exist, return -1
    @Test
    void testGetQuantity2() {
        Inventory<Object> i = generateTestInventory();
        assertThrows(ItemDoesNotExistException.class, () -> {i.getQuantity(Drink.TEST);});
    }

    // Test Case 1: add to existing stock and return the quantity + 1
    @Test
    void testAdd1() {
        Inventory<Object> i = generateTestInventory();
        i.add(Drink.LIME_SODA);
        assertEquals(1, i.getQuantity(Drink.LIME_SODA));
    }

    // Test Case 2: add a new item to inventory and update its stock
    @Test
    void testAdd2() {
        Inventory<Object> i = generateTestInventory();
        i.add(Drink.TEST);
        assertTrue(i.hasItem(Drink.TEST));
        assertEquals(1, i.getQuantity(Drink.TEST));
    }

    // Test Case 1: remove item from existing stock
    @Test
    void testRemove1() {
        Inventory<Object> i = generateTestInventory();
        i.remove(Drink.COLA);
        assertEquals(9, i.getQuantity(Drink.COLA));
    }

    // Test Case 2: remove an item that has 0 stock returns a SoldOutException
    @Test
    void testRemove2() {
        Inventory<Object> i = generateTestInventory();
        Exception exception = assertThrows(SoldOutException.class, () -> {i.remove(Drink.LIME_SODA);});
        String expectedMessage = "Sold Out";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    //Test Case 3: remove an item that does not exist in the inventory
    @Test
    void testRemove3() {
        Inventory<Object> i = generateTestInventory();
        Exception exception = assertThrows(ItemDoesNotExistException.class, () -> {i.remove(Drink.TEST);});
    }

    //Test Case 1: An item that has more than 0 stock in inventory
    @Test
    void testHasItem1() {
        Inventory<Object> i = generateTestInventory();
        assertTrue(i.hasItem(Drink.DIET_COLA));
    }

    //Test Case 2: An item does not exist in inventory
    @Test
    void testHasItem2() {
        Inventory<Object> i = generateTestInventory();
        Exception exception = assertThrows(ItemDoesNotExistException.class, () -> {i.hasItem(Drink.TEST);});
    }

    //Test Case 3: An item that has 0 stock in inventory
    @Test
    void testHasItem3() {
        Inventory<Object> i = generateTestInventory();
        assertFalse(i.hasItem(Drink.LIME_SODA));
    }

    //Test Case 1: Clear the inventory and check if the hashmep is empty
    @Test
    void testClear() {
        Inventory<Object> i = generateTestInventory();
        i.clear();
        assertTrue(i.isEmpty());
    }

    // Test Case 1: Checks a filled inventory to see if it is empty
    @Test
    void testIsEmpty1() {
        Inventory<Object> i = generateTestInventory();
        assertFalse(i.isEmpty());
    }

    //Test Case 2: Checks to see if a cleared inventory is empty
    @Test
    void testIsEmpty2() {
        Inventory<Object> i = generateTestInventory();
        i.clear();
        assertTrue(i.isEmpty());
    }

    // Test Case 1: updates the inventory for an existing item
    @Test
    void testPut1() {
        Inventory<Object> i = generateTestInventory();
        i.put(Drink.COLA, 4);
        assertEquals(4, i.getQuantity(Drink.COLA));
    }

    // Test Case 2: updates the inventory for a new item
    @Test
    void testPut2() {
        Inventory<Object> i = generateTestInventory();
        i.put(Drink.TEST, 5);
        assertEquals(5, i.getQuantity(Drink.TEST));
    }
}