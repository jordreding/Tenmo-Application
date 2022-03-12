package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransactionService transactionService = new TransactionService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private BigDecimal currentBalance;
    private final BigDecimal USER_DOES_NOT_EXIST_AMOUNT = new BigDecimal(-5);


    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService.setUser(currentUser);
            transactionService.setUser(currentUser);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
               // listUsers();
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance();
        currentBalance = balance;
        consoleService.printBalance(balance);
	}

	private void viewTransferHistory() {
		List<Transaction> transactions = transactionService.getAllApprovedTransactionRecords();
        consoleService.printApprovedTransactionRecords(transactions);
        int transferIdToView = consoleService.promptForInt("Please Enter Transfer ID to view details (0 to cancel): ");
        if (transactionService.transferIdExists(transactions, transferIdToView)) {
            Transaction transaction = transactionService.getTransactionByTransferId(transferIdToView);
            consoleService.printTransactionToView(transaction);
        } else {
            consoleService.transactionIdDoesNotExists();
        }
	}

	private void viewPendingRequests() {
        List<Transaction> transactions = transactionService.getAllPendingTransactionRecords();
        consoleService.printPendingTransactionRecords(transactions);
        int transferIdToView = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
        if (transactionService.transferIdExists(transactions, transferIdToView)) {
            Transaction transaction = transactionService.getTransactionByTransferId(transferIdToView);
 //           consoleService.printTransactionToView(transaction);
            int approveRejectChoice = consoleService.promptForInt("1: Approve\n2: Reject\n0: Don't approve or reject\n" +
                    "---------\n" + "Please choose an option: ");
            transactionService.approveOrReject(transaction, approveRejectChoice);
        } else {
            consoleService.transactionIdDoesNotExists();
        }

	}

	private void sendBucks() {
        List<User> userList = accountService.getAllUsersNotCurrentUser();
        consoleService.printAllUsersNotCurrentUser(userList);
        BigDecimal balance = accountService.getBalance();
        currentBalance = balance;
        Transaction transaction = consoleService.buildSentTransactionFromUser(currentBalance, userList);
        if (consoleService.ensureValidAmount(transaction)) {
            transactionService.addSendTransaction(transaction);
        }
	}


	private void requestBucks() {
        List<User> userList = accountService.getAllUsersNotCurrentUser();
        consoleService.printAllUsersNotCurrentUser(userList);
        Transaction requestTransaction = consoleService.buildPendingTransactionFromUser(userList);
        if (consoleService.ensureValidAmount(requestTransaction)) {
            transactionService.addRequestTransaction(requestTransaction);
        }
    }

}
