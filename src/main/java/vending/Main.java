package vending;

import vending.entities.Drink;
import vending.entities.Money;
import vending.exceptions.InsufficientChangeException;
import vending.exceptions.InsufficientFundsException;
import vending.exceptions.SoldOutException;
import vending.services.VendingMachine;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        String command,item,input;
        boolean running = true;

        System.out.println("Welcome! What Drink would you like today?");

        Scanner s = new Scanner(System.in);

        while (running) {
            input = s.nextLine();
            String[] processInput = input.split(",");
            command = processInput[0];

            try {

                if (command.equalsIgnoreCase("DEPOSIT")) {
                    item = processInput[1];

                    if (item.equalsIgnoreCase("QUARTER")) {
                        vm.addBalance(Money.QUARTER);
                        vm.printState();
                    } else if (item.equalsIgnoreCase("DIME")) {
                        vm.addBalance(Money.DIME);
                        vm.printState();
                    } else if (item.equalsIgnoreCase("NICKEL")) {
                        vm.addBalance(Money.NICKEL);
                        vm.printState();
                    } else {
                        System.out.println("Invalid Coin!");
                    }

                } else if (command.equalsIgnoreCase("SELECT")) {
                    item = processInput[1];

                    if (item.equalsIgnoreCase("COLA")) {
                        System.out.println("Cola Selected");
                        vm.getPrice(Drink.COLA);
                        List<Money> change = vm.collectChange();
                        Drink paidDrink = vm.collectDrink();
                        printReturn(paidDrink,change);
                        vm.printState();
                    } else if (item.equalsIgnoreCase("DIET_COLA")) {
                        System.out.println("Diet Cola Selected");
                        vm.getPrice(Drink.DIET_COLA);
                        List<Money> change = vm.collectChange();
                        Drink paidDrink = vm.collectDrink();
                        printReturn(paidDrink,change);
                        vm.printState();
                    } else if (item.equalsIgnoreCase("LIME_SODA")) {
                        System.out.println("Lime Soda Selected");
                        vm.getPrice(Drink.LIME_SODA);
                        List<Money> change = vm.collectChange();
                        Drink paidDrink = vm.collectDrink();
                        printReturn(paidDrink,change);
                        vm.printState();
                    } else if (item.equalsIgnoreCase("WATER")) {
                        System.out.println("Water Selected");
                        vm.getPrice(Drink.WATER);
                        List<Money> change = vm.collectChange();
                        Drink paidDrink = vm.collectDrink();
                        printReturn(paidDrink,change);
                        vm.printState();
                    } else {
                        System.out.println("Invalid Drink!");
                    }

                } else if (command.equalsIgnoreCase("CANCEL")) {
                    List<Money> refundedCoins = vm.refund();
                    System.out.println(refundedCoins + " refunded");
                    vm.printState();
                } else if (command.equalsIgnoreCase("OFF")) {
                    System.out.println("Goodbye");
                    running = false;
                } else {
                    System.out.println("Invalid Command!");
                }

            } catch (InsufficientChangeException e1) {
                System.out.println("Insufficient Change! Sorry please choose another drink");
            } catch (InsufficientFundsException e2) {
                System.out.println("Insufficient Funds. Please add " + e2.getRemaining() + " cents");
            } catch (SoldOutException e3) {
                System.out.println("Sold Out! Please choose another drink");
            }

        }

    }

    private static void printReturn(Drink paidDrink, List<Money> change) {
        System.out.println("Returned Change: " + change);
        System.out.println(paidDrink.getName() + " Dispensed");
    }

}