package vending.services;

import org.junit.jupiter.api.*;
import vending.entities.Drink;
import vending.entities.Money;
import vending.exceptions.InsufficientChangeException;
import vending.exceptions.InsufficientFundsException;
import vending.exceptions.ItemDoesNotExistException;
import vending.exceptions.SoldOutException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {

    private VendingMachine vm;

    @BeforeEach
    public void init() {
        vm = new VendingMachine();
    }

    @AfterEach
    public void clear() {
        vm = null;
    }
    /*
     * Vending Machine is initialized with
     * 10x Colas (25), 8x Diet Colas (35), 0x Lime Sodas (25) and 2x Waters (45)
     * 5x Nickels, 5x Dimes, and 5x Quarters for a total of 2 dollars or 200 cents
     * */

    // Test Case 1: given an existing item
    @Test
    void testGetPrice1() {
        long priceRetrieved = vm.getPrice(Drink.COLA);
        assertEquals(25, priceRetrieved);
        assertEquals(Drink.COLA, vm.getCurrentDrink());
    }

    // Test Case 2: given an out-of-stock item, return an SoldOutException
    @Test
    void testGetPrice2() {
        assertThrows(SoldOutException.class, () -> {vm.getPrice(Drink.LIME_SODA);});
        assertNull(vm.getCurrentDrink());
    }

    // Test Case 3: given a non-existent item, return ItemDoesNotExistException
    @Test
    void testGetPrice3() {
        assertThrows(ItemDoesNotExistException.class, () -> {vm.getPrice(Drink.TEST);});
        assertNull(vm.getCurrentDrink());
    }

    // Test Case 1: given a coin
    @Test
    void testAddBalance1() {
        vm.addBalance(Money.DIME);
        vm.addBalance(Money.QUARTER);
        List<Money> expected = new ArrayList<>();
        expected.add(Money.DIME);
        expected.add(Money.QUARTER);

        assertEquals(expected, vm.getDepositedCoins());
        assertEquals(35, vm.getCurrentBalance());
    }

    // Test Case 1: when coins have been deposited
    @Test
    void testRefund1() {
        vm.addBalance(Money.QUARTER);
        List<Money> expected = new ArrayList<>();
        expected.add(Money.QUARTER);

        List<Money> refund = vm.refund();

        assertTrue(vm.getDepositedCoins().isEmpty());
        assertEquals(0, vm.getCurrentBalance());
        assertNull(vm.getCurrentDrink());
        assertEquals(expected, refund);
    }

    // Test Case 2: when coins have not been deposited
    @Test
    void testRefund2() {
        List<Money> expected = new ArrayList<>();

        List<Money> refund = vm.refund();

        assertTrue(vm.getDepositedCoins().isEmpty());
        assertEquals(0, vm.getCurrentBalance());
        assertNull(vm.getCurrentDrink());
        assertEquals(expected, refund);
    }

    // Test Case 1: when there is sufficient change
    @Test
    void testHasSufficientChange1() {
        assertTrue(vm.hasSufficientChange(200));
    }

    // Test Case 2: when there is insufficient change
    @Test
    void testHasSufficientChange2() {
        assertFalse(vm.hasSufficientChange(201));
    }

    // Test Case 1: when there is a sufficient deposit to cover the drink chosen - Cola for 25 cents
    @Test
    void testIsFullyPaid1() {
        vm.getPrice(Drink.COLA);
        vm.addBalance(Money.QUARTER);
        assertTrue(vm.hasSufficientFunds());
    }

    // Test Case 2: when there is an insufficient deposit to cover the drink chosen - Cola with 10 cents
    @Test
    void testIsFullyPaid2() {
        vm.getPrice(Drink.COLA);
        vm.addBalance(Money.DIME);
        Exception e = assertThrows(InsufficientFundsException.class, () -> {vm.hasSufficientFunds();});
    }

    // Test Case 1: there is enough change for the amount requested in this case 50
    @Test
    void testGetChange1() {
        List<Money> change = vm.getChange(60);
        List<Money> expected = new ArrayList<>();
        expected.add(Money.QUARTER);
        expected.add(Money.QUARTER);
        expected.add(Money.DIME);

        assertEquals(expected,change);
    }

    // Test Case 2: there not enough change for the requested amount
    @Test
    void testGetChange2() {
        Exception e = assertThrows(InsufficientChangeException.class, () -> {vm.getChange(1000);});
    }

    // Test Case 1: There is sufficient funds and sufficient change for the transaction to be completed
    @Test
    void testCollectChange1() {
        vm.addBalance(Money.QUARTER);
        vm.addBalance(Money.QUARTER);
        vm.getPrice(Drink.COLA);

        List<Money> expected = new ArrayList<>();
        expected.add(Money.QUARTER);
        List<Money> actual = vm.collectChange();

        assertEquals(expected,actual);
        assertEquals(0,vm.getCurrentBalance());
        assertTrue(vm.isPaid());
    }

    // Test Case 2: There are insufficient funds
    @Test
    void testCollectChange2() {
        vm.addBalance(Money.DIME);
        vm.getPrice(Drink.COLA);

        assertEquals(10, vm.getCurrentBalance());
        assertThrows(InsufficientFundsException.class, () -> {vm.collectChange();});
    }

    //Test Case 3: There is insufficient change
    @Test
    void testCollectChange3() {
        for (int i = 0; i < 5; i++) {
            vm.addBalance(Money.QUARTER);
            vm.addBalance(Money.QUARTER);
            vm.getPrice(Drink.DIET_COLA);
            vm.collectChange();
        }

        vm.addBalance(Money.QUARTER);
        vm.addBalance(Money.QUARTER);
        vm.getPrice(Drink.DIET_COLA);
        assertThrows(InsufficientChangeException.class, () -> {vm.collectChange();});
    }

    // Test Case 1: collecting the drink after paying for it
    @Test
    void testCollectDrink1() {
        vm.addBalance(Money.QUARTER);
        vm.addBalance(Money.QUARTER);
        vm.getPrice(Drink.DIET_COLA);
        vm.collectChange();
        Drink actual = vm.collectDrink();

        assertFalse(vm.isPaid());
        assertEquals(Drink.DIET_COLA,actual);
    }

    // Test Case 2: collecting drink without paying for it
    @Test
    void testCollectDrink2() {
        vm.addBalance(Money.QUARTER);
        vm.addBalance(Money.QUARTER);
        vm.getPrice(Drink.DIET_COLA);
        Drink actual = vm.collectDrink();
        assertNull(actual);
    }
}