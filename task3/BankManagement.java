package task3;

import java.io.*;
import java.util.*;

class BankAccount {
    private String accountNumber;
    private String holderName;
    private double balance;

    public BankAccount(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("Account No: " + accountNumber + ", Name: " + holderName + ", Balance: ₹" + balance);
    }

    public String toFileString() {
        return accountNumber + "," + holderName + "," + balance;
    }

    public static BankAccount fromFileString(String line) {
        String[] parts = line.split(",");
        return new BankAccount(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

public class BankManagement {
    private static final String FILE_NAME = "accounts.txt";
    private static List<BankAccount> accounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccountsFromFile();

        int choice;
        do {
            System.out.println("\n🏦 Bank Management System");
            System.out.println("1. Create Account");
            System.out.println("2. View All Accounts");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> viewAccounts();
                case 3 -> deposit();
                case 4 -> withdraw();
                case 5 -> {
                    saveAccountsToFile();
                    System.out.println("Thank you for using the Bank Management System.");
                }
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    private static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accNo)) {
                System.out.println("Account with this number already exists!");
                return;
            }
        }
        System.out.print("Enter Holder Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        accounts.add(new BankAccount(accNo, name, balance));
        System.out.println("Account created successfully.");
    }

    private static void viewAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (BankAccount acc : accounts) {
                acc.display();
            }
        }
    }

    private static void deposit() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            acc.deposit(amount);
            System.out.println("Deposit successful.");
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdraw() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            if (acc.withdraw(amount)) {
                System.out.println("Withdrawal successful.");
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static BankAccount findAccount(String accNo) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accNo)) {
                return acc;
            }
        }
        return null;
    }

    private static void loadAccountsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                accounts.add(BankAccount.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("No existing accounts found.");
        }
    }

    private static void saveAccountsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (BankAccount acc : accounts) {
                writer.println(acc.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts.");
        }
    }
}
