package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final BigDecimal USER_DOES_NOT_EXIST_AMOUNT = new BigDecimal(-5);

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

    public Transaction buildSentTransactionFromUser(BigDecimal currentBalance, List<User> userList) {
        Transaction transaction = new Transaction();
        transaction.setUserIdTo(promptForInt("Enter ID of user you are sending to (0 to cancel): "));
        BigDecimal amount = promptForBigDecimal("Enter amount: ");
        setProperTransactionAmountOrErrorAmounts(transaction, userList, amount, currentBalance);
        transaction.setTransfer_status_id(2);
        transaction.setTransfer_type_id(2);
        return transaction;
    }

    public Transaction buildPendingTransactionFromUser(List<User> userList) {
        Transaction transaction = new Transaction();
        transaction.setUserIdTo(promptForInt("Enter ID of user you are requesting from (0 to cancel): "));
        BigDecimal amount = promptForBigDecimal("Enter amount: ");
        setProperTransactionAmountOrErrorAmounts(transaction, userList, amount);
        transaction.setTransfer_status_id(1);
        transaction.setTransfer_type_id(1);
        return transaction;
    }


    public void setProperTransactionAmountOrErrorAmounts(Transaction transaction, List<User> userList, BigDecimal amount, BigDecimal currentBalance) {
        if (hasSufficientFunds(amount, currentBalance) && isNotZeroOrNegative(amount)) {
            if (userListContainsUserToSend(userList, transaction.getUserIdTo())) {
                transaction.setAmount(amount);
            } else {
                transaction.setAmount(USER_DOES_NOT_EXIST_AMOUNT);
            }
        } else {
            transaction.setAmount(new BigDecimal(0));
        }
    }

    public void setProperTransactionAmountOrErrorAmounts(Transaction transaction, List<User> userList, BigDecimal amount) {
        if (isNotZeroOrNegative(amount)) {
            if (userListContainsUserToSend(userList, transaction.getUserIdTo())) {
                transaction.setAmount(amount);
            } else {
                transaction.setAmount(USER_DOES_NOT_EXIST_AMOUNT);
            }
        } else {
            transaction.setAmount(new BigDecimal(0));
        }
    }

    public boolean ensureValidAmount(Transaction transaction) {
        if (transaction.getAmount().compareTo(USER_DOES_NOT_EXIST_AMOUNT) != 0) {
            if (transaction.getAmount().compareTo(new BigDecimal(0)) == 1) {
                return true;
            } else {
                printInsufficientOrNegativeAmount();
                return false;
            }
        } else {
            printNonexistentUser();
            return false;
        }
    }

    private boolean userListContainsUserToSend(List<User> userList, int userId) {
        for (User user: userList) {
            if (user.getId() == userId) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSufficientFunds(BigDecimal amount, BigDecimal currentBalance) {
        return currentBalance.compareTo(amount) > -1;
    }

    public boolean isNotZeroOrNegative(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) == 1;
    }

    public void printInsufficientOrNegativeAmount() {
        System.out.println("Please enter a positive number less than your current balance.");
    }

    public void printNonexistentUser() {
        System.out.println("Please enter a user Id from the list above.");
    }

    public void printApprovedTransactionRecords(List<Transaction> records) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Transfers");
        System.out.printf("%-15s%-25s%-40s\n", "ID", "From/To", "Amount");
        System.out.println("--------------------------------------------------------");
        for (Transaction record : records) {
            String transferType = record.getTransferType();
            String toName = record.getToName();
            String fromName = record.getFromName();
            if (record.getTransferType().equalsIgnoreCase("TO")) {
                System.out.printf("%-15s%-4s: %-19s$ %-10.2f\n", record.getTransferId(), transferType, toName, record.getAmount());
            } else {
                System.out.printf("%-15s%-4s: %-19s$ %-10.2f\n", record.getTransferId(), transferType, fromName, record.getAmount());
            }
        }
        System.out.println("-----------------------------");
    }

    public void printPendingTransactionRecords(List<Transaction> records) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.printf("%-19s%-25s%-40s\n", "ID", "To", "Amount");
        System.out.println("--------------------------------------------------------");

        for (Transaction record : records) {
            System.out.printf("%-19s%-25s$ %-10.2f\n", record.getTransferId(),  record.getFromName(), record.getAmount());
        }
        System.out.println("-----------------------------");

    }

    public void printTransactionToView(Transaction transaction) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------------------");
        System.out.println("Id: " + transaction.getTransferId());
        System.out.println("From: " + transaction.getFromName());
        System.out.println("To: " + transaction.getToName());
        System.out.println("Type: " + getTypeFromTypeId(transaction.getTransfer_type_id()));
        System.out.println("Status: " + getStatusFromStatusId(transaction.getTransfer_status_id()));
        System.out.printf("Amount: $%.2f\n", transaction.getAmount());
    }

    private String getTypeFromTypeId(int typeId) {
        if (typeId == 2) {
            return "Send";
        } else {
            return "Request";
        }
    }

    private String getStatusFromStatusId(int statusId) {
        if (statusId == 1) {
            return "Pending";
        } else if (statusId == 2) {
            return "Approved";
        } else {
            return "Rejected";
        }
    }

    public void transactionIdDoesNotExists() {
        System.out.println("Transfer Id does not exist. Please choose an Id from the above list.");
    }

}
