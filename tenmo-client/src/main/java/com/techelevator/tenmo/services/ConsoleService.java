package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
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

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public Transfer promptForSendingInfo(User[] users, long userId, BigDecimal balance) {
        //check to make sure that userID being entered is valid (not a username, and not a userId that does not exist)
        String id = "0";
        String amount = "0";
        Transfer transfer = new Transfer(0, userId, Long.parseLong(id), "Approved", "Send", new BigDecimal(amount));
        do {
            id = promptForString("Enter ID of user you are sending to (0 to cancel): ");
            if (id.equals("0")) {
                return transfer;
            }
            if (!isNumberId(id)) {
                printErrorMessage("An error occurred. Check the log for details.");
            } else {
                if (Long.parseLong(id) == userId) {
                    printErrorMessage("You must not be allowed to send money to yourself.");
                    id = "";
                } else {
                    int i = 1;
                    for (User user : users) {
                        if (String.valueOf(user.getId()).equals(id)) {
                            break;
                        } else if (users.length == i) {
                            id = "error";
                        }
                        i++;
                    }
                }
            }
            if (id.equals("error")) {
                printErrorMessage("An error occurred. Check the log for details.");
            }
        } while (!isNumberId(id));

        transfer.setToAccount(Long.parseLong(id));

        do {
            amount = promptForString("Enter amount: ");
            if (isNumberAmount(amount)) {
                if (new BigDecimal(amount).compareTo(balance) == 1) {
                    printErrorMessage("You don't have enough TE Bucks in your account.");
                    amount = "0";
                }
            } else {
                printErrorMessage("An error occurred. Check the log for details.");
            }
        } while (!isNumberAmount(amount));

        transfer.setAmount(new BigDecimal(amount));
        return transfer;
    }

    private boolean isNumberId(String id) {
        try {
            int number = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean isNumberAmount(String id) {
        try {
            BigDecimal number = new BigDecimal(id);
            if (number.compareTo(new BigDecimal("0"))<0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void printCurrentBalance(BigDecimal balance) {
        System.out.println("Your current account balance is: $" + balance);
    }

    public void printForUserList(User[] users, Long id) {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------------------");
        for (User user : users) {
            if (!user.getId().equals(id)) {
                System.out.println(user.getId() + "          " + user.getUsername());
            }
        }
        System.out.println("-------------------------------------------");
    }

    public String printTransferHistory(Transfer[] transfers, String name) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To          Amount");
        System.out.println("-------------------------------------------");

        for (Transfer transfer : transfers) {
            if (transfer.getSender().equals(name)) {
                System.out.format("%-12d%-17s%s%.2f%n", transfer.getId(),"To: " + transfer.getReceiver(), "$", transfer.getAmount());
            } else if (transfer.getReceiver().equals(name)) {
                System.out.format("%-12d%-17s%s%.2f%n", transfer.getId(),"From: " + transfer.getSender(), "$", transfer.getAmount());
            }
        }
        System.out.println("-------------------------------------------");
        String id = "0";
        do {
            id = promptForString("Please enter transfer ID to view details (0 to cancel): ");
            if (id.equals("0")) {
                return id;
            }
            if (!isNumberId(id)) {
                printErrorMessage("An error occurred. Check the log for details.");
            } else {
                int i = 1;
                for (Transfer transfer : transfers) {
                    if (String.valueOf(transfer.getId()).equals(id)) {
                        break;
                    } else if (transfers.length == i) {
                        id = "error";
                    }
                    i++;
                }
                if (id.equals("error")) {
                    printErrorMessage("An error occurred. Check the log for details.");
                }
            }
        } while (!isNumberId(id));
        return id;
    }

    public void printTransferDetail(Transfer transfer, String name) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfers Details");
        System.out.println("-------------------------------------------");
        System.out.println("Id: " + transfer.getId());
        if (transfer.getSender().equals(name)) {
            System.out.println("From: Me Myself and I");
        } else {
            System.out.println("From: " + transfer.getSender());
        }
        if (transfer.getReceiver().equals(name)) {
            System.out.println("To: Me Myself and I");
        } else {
            System.out.println("To: " + transfer.getReceiver());
        }
        System.out.println("Type: " + transfer.getType());
        System.out.println("Status: " + transfer.getStatus());
        System.out.println("Amount: $" + transfer.getAmount());
    }
}
