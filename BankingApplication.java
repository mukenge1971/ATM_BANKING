/**
 * BankingApplication
 */
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.Scanner;
public class BankingApplication {
 


    private static Map<String, BankAccount> accounts = new HashMap<>();
    

    public static void main(String[] args) {
        loadAccounts();
        Scanner scanner = new Scanner(System.in);
        String option;

        do {
            System.out.println("==========================================================");
            System.out.println("Banking Application");
            System.out.println("1. Create Account");
            System.out.println("2. Access Account");
            System.out.println("3. Show accounts");
            System.out.println("4. Exit");
            System.out.println("==========================================================");
            System.out.print("Enter an option: ");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    createAccount(scanner);
                    break;
                case "2":
                    accessAccount(scanner);
                    break;
                    case "3":
                    listAccounts();
                    break;
                case "4":
                    saveAccounts();
                    System.out.println("Thank you for using our services");
                    break;
                default:
                    System.out.println("Invalid option, try again");
                    break;
            }
        } while (!option.equals("4"));
        scanner.close(); // Fermer le scanner après utilisation

    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer ID: ");
        String id = scanner.nextLine();

        if (accounts.containsKey(id)) {
            System.out.println("Account with this ID already exists.");
            listAccounts();

        } else {
            BankAccount account = new BankAccount(name, id);
            accounts.put(id, account);
            System.out.println("Account created successfully.");
            listAccounts();
        }
    }

    private static void accessAccount(Scanner scanner) {
        System.out.print("Enter customer ID: ");
        String id = scanner.nextLine();

        BankAccount account = accounts.get(id);
        if (account != null) {
            account.showMenu(scanner);
            listAccounts();
        } else {
            System.out.println("Account not found.");
        }
    }
    private static void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            System.out.println("List of accounts:");
            for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
                BankAccount account = entry.getValue();
                System.out.println("Customer ID: " + account.getCustomerId());
                System.out.println("Customer Name: " + account.getCustomerName());
                System.out.println("Balance: " + account.getBalance());
                System.out.println("--------------------------------");
            }
        }
    }

    private static void saveAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
            for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
                BankAccount account = entry.getValue();
                writer.write(account.getCustomerId() + "," + account.getCustomerName() + "," + account.getBalance() + "," + account.getPreviousTransaction() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    int balance = Integer.parseInt(parts[2]);
                    int previousTransaction = Integer.parseInt(parts[3]);
                    BankAccount account = new BankAccount(name, id);
                    account.setBalance(balance);
                    account.setPreviousTransaction(previousTransaction);
                    accounts.put(id, account);
                }
            }
        } catch (IOException | NumberFormatException e) {
            // Si le fichier n'existe pas ou est corrompu, initialiser les valeurs par défaut
            accounts = new HashMap<>();
        }
    }
    
    
}


/**
 * InnerBankingApplication
 */
 class BankAccount {

    int balance;
    int previousTransaction;
    String customerName;
    String customerId;


    BankAccount(String custName, String custId) {

        this.customerName = custName;
        this.customerId = custId;

        loadData();
    }

    void deposit(int amount) {
        if (amount != 0) {

            this.balance += amount;
            this.previousTransaction = amount;
            saveData();
        }
    }

    void withdraw(int amount) {

        if (amount != 0) {

            this.balance -= amount;
            this.previousTransaction = -amount;
            saveData();
        }
    }

    void showPreviousTransaction() {

        if (this.previousTransaction > 0) {

            System.out.println("Deposited : "+ this.previousTransaction);
        } else if (this.previousTransaction < 0) {
            System.out.println("Withdrawn : "+ Math.abs(this.previousTransaction));
        } else {
            System.out.println("No transaction occured");
        }
    }

    

    private void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bank_data_" + this.customerId + ".txt"))) {
            writer.write(this.balance + "\n");
            writer.write(this.previousTransaction + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("bank_data_" + this.customerId + ".txt"))) {
            this.balance = Integer.parseInt(reader.readLine());
            this.previousTransaction = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            // Si le fichier n'existe pas ou est corrompu, initialiser les valeurs par défaut
            this.balance = 0;
            this.previousTransaction = 0;
        }
    }

    void showMenu(Scanner scanner) {
        char option = '\0';

        //Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome "+this.customerName);
        System.out.println("Your ID is "+ this.customerId);
        System.out.println("\n");
        System.out.println("A Check balance");
        System.out.println("B Deposit");
        System.out.println("C Withdraw");
        System.out.println("D Previous transaction");
        System.out.println("E Exit");

        do 
        {
            System.out.println("==========================================================");
            System.out.println("Enter an option");
            System.out.println("==========================================================");
            option = scanner.next().charAt(0);
            System.out.println("\n");

            switch(option) {

                case 'A':
            
                System.out.println("--------------------------------");
                System.out.println("The balance is : "+ this.balance);
                System.out.println("\n");
                System.out.println("--------------------------------");


                break;
                case 'B':

                System.out.println("--------------------------------");
                System.out.println("Enter an amount deposit :");
                System.out.println("--------------------------------");
                int amount = scanner.nextInt();
                deposit(amount);
                System.out.println("\n");


                break;

                case 'C': 

                System.out.println("--------------------------------");
                System.out.println("Enter a withdraw amount :");
                System.out.println("--------------------------------");
                int amount2 = scanner.nextInt();
                withdraw(amount2);
                System.out.println("\n");

                break;

                case 'D': 

                System.out.println("--------------------------------");
                showPreviousTransaction();
                System.out.println("\n");
                System.out.println("\n");

                break;

                case 'E':
                System.out.println("=================================================");
                break;
                default:
                System.out.println("Invalid character, try again");
                break;

                
            }
            
        } while (option != 'E');

        System.out.println("Thank you for using our services");
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getBalance() {
        return balance;
    }
    public int getPreviousTransaction() {
        return previousTransaction;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setPreviousTransaction(int previousTransaction) {
        this.previousTransaction = previousTransaction;
    }
}