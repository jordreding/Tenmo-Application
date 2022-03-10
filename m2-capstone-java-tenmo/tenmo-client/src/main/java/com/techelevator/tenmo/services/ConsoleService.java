package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printUserDoesNotExistMessage() {
        System.out.println("User does not exist, please enter a valid user id.");
    }

    public void printBalance(BigDecimal balance) {
        System.out.println("Your current account balance is: $" + balance);
    }

    public void printAllUsersNotCurrentUser(List<User> userList) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Users");
        System.out.printf("%-15s%-25s\n", "ID", "Name");
        System.out.println("--------------------------------------------------------");

        for (User user : userList) {
            System.out.printf("%-15d%-25s\n", user.getId(), user.getUsername());
        }
        System.out.println("-----------------------------");
    }

    public Transaction getTransactionFromUser(BigDecimal currentBalance) {
        Transaction transaction = new Transaction();
        transaction.setUserIdTo(promptForInt("Enter ID of user you are sending to (0 to cancel): "));
        transaction.setAmount(promptForBigDecimal("Enter amount: "));
        transaction.setTransfer_status_id(2);
        transaction.setTransfer_type_id(2);
        return transaction;
    }

//    public double promptForAmountToSend(BigDecimal currentBalance) {
//
//        System.out.println("Enter amount: ");
//        String strAmount = scanner.nextLine();
//        double amount = Double.parseDouble(strAmount);
//        return amount;
//    }
//
//    public int promptForRecipientUserId() {
//        System.out.println("Enter ID of user you are sending to (0 to cancel): ");
//        String strId = scanner.nextLine();
//        int id = Integer.parseInt(strId);
//        return id;
//    }

    public boolean hasSufficientFunds(double amount, BigDecimal currentBalance) {
        return true;
    }

    public boolean isNotZeroOrNegative(double amount) {
        return amount > 0;
    }

}
