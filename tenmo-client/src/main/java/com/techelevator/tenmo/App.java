package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
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
            consoleService.printErrorMessage("An error occurred. Check the log for details.");
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage("An error occurred. Check the log for details.");
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
        BigDecimal balance = accountService.getBalance(currentUser);
        consoleService.printCurrentBalance(balance);
    }

    private void viewTransferHistory() {
        Transfer[] transfers = transferService.transferList(currentUser);
        String userName = currentUser.getUser().getUsername();
        if (transfers != null) {
            String transferId = consoleService.printTransferHistory(transfers, userName);
            if (transferId != null && !transferId.equals("0")) {
                Transfer transfer = transferService.getTransferDetail(transferId, currentUser);
                consoleService.printTransferDetail(transfer, userName);
            }
        }
    }

    private void sendBucks() {
        User[] users = transferService.list(currentUser);
        long userId = currentUser.getUser().getId();
        if (users != null) {
            consoleService.printForUserList(users, userId);
        }

        BigDecimal balance = accountService.getBalance(currentUser);
        Transfer transfer = consoleService.promptForSendingInfo(users, userId, balance);
        transferService.transfer(transfer, currentUser);

        if (currentUser == null) {
            consoleService.printErrorMessage("An error occurred. Check the log for details.");
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
    }
}
