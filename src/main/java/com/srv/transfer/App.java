package com.srv.transfer;

import com.srv.transfer.exceptions.NotFoundException;
import com.srv.transfer.service.AccountService;
import com.srv.transfer.service.DefaultAccountService;
import com.srv.transfer.service.TransferService;
import com.srv.transfer.utils.CommandExecutionHelper;
import com.srv.transfer.utils.InputUtils;

import java.io.IOException;

/**
 * Application starting point
 */
public class App {
    public static void main(String[] args) throws IOException {

        // Initialization
        AccountService accountService = new DefaultAccountService();
        TransferService transferService = new TransferService(accountService);
        CommandExecutionHelper commandExecutionHelper = new CommandExecutionHelper(accountService, transferService);

        System.out.println("=== Welcome to account transfer service ===");
        boolean stop = false;
        while (!stop) {
            try {
                help();
                String cmd = InputUtils.takeInput("Enter command number or Quoted Character: ");
                switch (cmd) {
                    case "1":
                    case "c":
                    case "C":
                        commandExecutionHelper.beginAccountCreation();
                        break;
                    case "2":
                    case "t":
                    case "T":
                        commandExecutionHelper.beginTransaction();
                        break;
                    case "3":
                    case "l":
                    case "L":
                        commandExecutionHelper.listTransactions();
                        break;
                    case "4":
                    case "a":
                    case "A":
                        commandExecutionHelper.listAccountTransactions();
                        break;
                    case "5":
                    case "q":
                    case "Q":
                        stop = true;
                        System.out.println("Thanks you for using this service.");
                        break;
                    default:
                        System.out.println("Invalid command.\b");
                        break;
                }
            } catch (NotFoundException notFoundException) {
                System.err.println("[ERROR] Account not found : " + notFoundException.getMessage() + "\nPlease create account first.");
            } catch (Exception exc) {
                System.out.println("[ERROR] Unknown error occured");
                exc.printStackTrace();
                stop = true;
            }
        }
    }

    static private void help() {
        System.out.println("\nEnter any of the below command and press Enter.");
        System.out.println("\t1. Create account 'C'");
        System.out.println("\t2. Account transfer 'T'");
        System.out.println("\t3. All Transactions list 'L'");
        System.out.println("\t4. Account transactions 'A'");
        System.out.println("\t5. Quit the program 'Q'\n");
    }
}
