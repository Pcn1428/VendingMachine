package vending.services;

import vending.entities.Drink;
import vending.entities.Money;
import vending.exceptions.InsufficientChangeException;
import vending.exceptions.InsufficientFundsException;
import vending.exceptions.SoldOutException;
import vending.repositories.Inventory;

import java.util.*;

public class VendingMachine {
    private Inventory<Money> Coins = new Inventory<>();
    private Inventory<Drink> Drinks = new Inventory<>();
    private List<Money> depositedCoins = new ArrayList<>();
    private Inventory<Drink> soldDrinks = new Inventory<>();
    private Drink currentDrink;
    private long currentBalance;
    private boolean paid;

    public VendingMachine() {
        init();
    }

    /*
    * Initialize the vending machine with
    * 10x Colas, 8x Diet Colas, 0x Lime Sodas and 2x Waters
    * 5x Nickels, 5x Dimes, and 5x Quarters
    * */
    private void init() {
        for (Money m : Money.values()) Coins.put(m, 5);
        for (Drink d : Drink.values()) soldDrinks.put(d, 0);
        Drinks.put(Drink.COLA,10);
        Drinks.put(Drink.DIET_COLA, 8);
        Drinks.put(Drink.LIME_SODA, 0);
        Drinks.put(Drink.WATER, 2);
        paid = false;
    }

    // Given an item, the price is displayed if it is in stock
    public long getPrice (Drink drink) throws SoldOutException{
        if(Drinks.hasItem(drink)) {
            currentDrink = drink;
            return currentDrink.getPrice();
        } else {
            throw new SoldOutException("Sold Out");
        }
    }

    // Adds inserted money, to the deposit inventory
    public void addBalance (Money money) {
        currentBalance += money.getValue();
        depositedCoins.add(money);
    }

    public List<Money> refund() {
        if (depositedCoins.isEmpty()) {
            return depositedCoins;
        } else {
            List<Money> refund = new ArrayList<>(depositedCoins);
            depositedCoins.clear();
            currentBalance = 0;
            return refund;
        }
    }

    // Dispenses the drink after confirming payment status
    public Drink collectDrink() {
        if (paid) {
            paid = false;
            soldDrinks.add(currentDrink);
            Drinks.remove(currentDrink);
            return currentDrink;
        } else {
            return null;
        }
    }

    // Returns true if there is enough of a deposit to cover the cost of the drink or returns an InsufficientFundsException otherwise
    public boolean hasSufficientFunds(){
        if (currentBalance >= currentDrink.getPrice()) {
            return true;
        } else {
            throw new InsufficientFundsException("Need more funds", currentDrink.getPrice()-currentBalance);
        }
    }

    // Returns true if there is enough change to cover the transaction and false otherwise
    public boolean hasSufficientChange(long change) {
        try {
            getChange(change);
        }
        catch(InsufficientChangeException e) {
            return false;
        }
        return true;
    }


    // Confirms the transaction and returns a list of coins that serve as change for the customer
    public List<Money> collectChange() throws InsufficientChangeException, InsufficientFundsException {
        long difference = currentBalance - currentDrink.getPrice();
        if (hasSufficientFunds()) {
            addDepositToInventory(depositedCoins);
            List<Money> change = getChange(difference);
            removeChangeFromInventory(change);
            depositedCoins.clear();
            currentBalance = 0;
            paid = true;
            return change;
        } else {
            return null;
        }
    }

    private void removeChangeFromInventory(List<Money> change) {
        for (Money m : change) {
            Coins.remove(m);
        }
    }

    private void addDepositToInventory(List<Money> deposit) {
        for (Money m : deposit) {
            Coins.add(m);
        }
    }

    /*
    * Returns a List of Money objects given the amount needed from the inventory of Coins or
    * throws an InsufficientChangeException if there is not enough change
    * */
    public List<Money> getChange(long amount) throws InsufficientChangeException {
        List<Money> change = new ArrayList<>();
        int numOfQuarters = Coins.getQuantity(Money.QUARTER);
        int numOfDimes = Coins.getQuantity(Money.DIME);
        int numOfNickels = Coins.getQuantity(Money.NICKEL);

        if (amount > 0) {
            change = new ArrayList<>();
            long balance = amount;
            while (balance > 0) {
                if (balance >= Money.QUARTER.getValue()
                        && Coins.hasItem(Money.QUARTER)
                        && numOfQuarters > 0) {
                  change.add(Money.QUARTER);
                  balance = balance - Money.QUARTER.getValue();
                  numOfQuarters--;

                } else if (balance >= Money.DIME.getValue()
                        && Coins.hasItem(Money.DIME)
                        && numOfDimes > 0) {
                  change.add(Money.DIME);
                  balance = balance - Money.DIME.getValue();
                  numOfDimes--;

                } else if (balance >= Money.NICKEL.getValue()
                        && Coins.hasItem(Money.NICKEL)
                        && numOfNickels > 0) {
                    change.add(Money.NICKEL);
                    balance = balance - Money.NICKEL.getValue();
                    numOfNickels--;
                } else {
                    throw new InsufficientChangeException("Sorry! Out of Change");
                }
            }
        }

        return change;
    }

    // Returns State of the Vending Machine
    public void printState() {
        String coinInventory = String.format("Coins: %d x Quarters, %d x Dimes, %d x Nickels",
                Coins.getQuantity(Money.QUARTER), Coins.getQuantity(Money.DIME), Coins.getQuantity(Money.NICKEL));
        String drinkInventory = String.format("Drinks: %d x Colas, %d x Diet Colas, %d x Lime Sodas, %d x Waters",
                Drinks.getQuantity(Drink.COLA), Drinks.getQuantity(Drink.DIET_COLA), Drinks.getQuantity(Drink.LIME_SODA), Drinks.getQuantity(Drink.WATER));
        String sales = String.format("Drinks Sold: %d x Colas, %d x Diet Colas, %d x Lime Sodas, %d x Waters",
                soldDrinks.getQuantity(Drink.COLA), soldDrinks.getQuantity(Drink.DIET_COLA), soldDrinks.getQuantity(Drink.LIME_SODA), soldDrinks.getQuantity(Drink.WATER));
        String deposit = String.format("Coins deposited: %s",depositedCoins);
        String balance = String.format("Current balance: %d", currentBalance);

        System.out.println(balance);
        System.out.println(deposit);
        System.out.println(coinInventory);
        System.out.println(drinkInventory);
        System.out.println(sales);
    }

    // Getters for testing

    public List<Money> getDepositedCoins() {
        return depositedCoins;
    }

    public long getCurrentBalance() {
        return currentBalance;
    }

    public Drink getCurrentDrink() {
        return currentDrink;
    }

    public boolean isPaid() {
        return paid;
    }


}
