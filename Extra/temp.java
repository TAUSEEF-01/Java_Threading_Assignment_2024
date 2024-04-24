import java.util.concurrent.*;

// Define the Account2 class
class Account2 {
    public Account2(String accountHolderName2, String accountNumber2, int maximumTransactionLimit2) {
        //TODO Auto-generated constructor stub
    }
    String accountHolderName;
    String accountNumber;
    int maximumTransactionLimit;
    // constructor and other methods...
}

// Define the Transaction class
class Transaction {
    String accountNumber;
    int amount;
    // constructor and other methods...
}

// Define the AccountGenerationThread class
class AccountGenerationThread extends Thread {
    // Shared data structure to hold the accounts
    BlockingQueue<Account2> accounts;
    // constructor and other methods...
    public void run() {
        // generate accounts and add to the queue
    }
}

// Define the DepositGenerationThread class
class DepositGenerationThread extends Thread {
    // Shared data structure to hold the deposit transactions
    BlockingQueue<Transaction> depositTransactions;
    // constructor and other methods...
    public void run() {
        // generate deposit transactions and add to the queue
    }
}

// Define the WithdrawGenerationThread class
class WithdrawGenerationThread extends Thread {
    // Shared data structure to hold the withdraw transactions
    BlockingQueue<Transaction> withdrawTransactions;
    // constructor and other methods...
    public void run() {
        // generate withdraw transactions and add to the queue
    }
}

// Define the DepositProcessingThread class
class DepositProcessingThread extends Thread {
    // Shared data structures to hold the accounts and deposit transactions
    BlockingQueue<Account2> accounts;
    BlockingQueue<Transaction> depositTransactions;
    // constructor and other methods...
    public void run() {
        // process deposit transactions
    }
}

// Define the WithdrawProcessingThread class
class WithdrawProcessingThread extends Thread {
    // Shared data structures to hold the accounts and withdraw transactions
    BlockingQueue<Account2> accounts;
    BlockingQueue<Transaction> withdrawTransactions;
    // constructor and other methods...
    public void run() {
        // process withdraw transactions
    }
}

// Define the main class
public class temp {
    public static void main(String[] args) {
        // Create shared data structures
        BlockingQueue<Account2> accounts = new LinkedBlockingQueue<>();
        BlockingQueue<Transaction> depositTransactions = new LinkedBlockingQueue<>();
        BlockingQueue<Transaction> withdrawTransactions = new LinkedBlockingQueue<>();

        // Create and start threads
        new AccountGenerationThread(accounts).start();
        new DepositGenerationThread(accounts, depositTransactions).start();
        new WithdrawGenerationThread(accounts, withdrawTransactions).start();
        new DepositProcessingThread(accounts, depositTransactions).start();
        new WithdrawProcessingThread(accounts, withdrawTransactions).start();
    }
}


























// import java.util.*;
// import java.util.concurrent.*;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.stream.Collectors;

// // Account2 class to store account information
// class Account2 {
//     String accountHolderName;
//     String accountNumber;
//     int maxTransactionLimit;
//     AtomicInteger balance;

//     public Account2(String name, String accountNumber, int limit) {
//         this.accountHolderName = name;
//         this.accountNumber = accountNumber;
//         this.maxTransactionLimit = limit;
//         this.balance = new AtomicInteger(0);
//     }
// }

// // Transaction class for deposit/withdraw transactions
// class Transaction {
//     String accountNumber;
//     int amount;

//     public Transaction(String accountNumber, int amount) {
//         this.accountNumber = accountNumber;
//         this.amount = amount;
//     }
// }

// // Account2 generation thread to create accounts
// class AccountGenerationThread extends Thread {
//     List<Account2> accounts;
//     int accountCount;

//     public AccountGenerationThread(List<Account2> accounts, int count) {
//         this.accounts = accounts;
//         this.accountCount = count;
//     }

//     @Override
//     public void run() {
//         Random random = new Random();
//         for (int i = 0; i < accountCount; i++) {
//             String name = "Holder" + (i + 1);
//             String accountNumber = "AC" + random.nextInt(1000000000) + random.nextInt(100);
//             int limit = random.nextInt(1000) + 1; // 1-1000 BDT
//             accounts.add(new Account2(name, accountNumber, limit));
//             try {
//                 Thread.sleep(1000); // Sleep for 1 second before creating next account
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }
//     }
// }

// // Thread to generate deposit transactions
// class DepositGenerationThread extends Thread {
//     List<Transaction> depositTransactions;
//     List<Account2> accounts;
//     int transactionCount;

//     public DepositGenerationThread(List<Transaction> depositTransactions, List<Account2> accounts, int count) {
//         this.depositTransactions = depositTransactions;
//         this.accounts = accounts;
//         this.transactionCount = count;
//     }

//     @Override
//     public void run() {
//         Random random = new Random();
//         for (int i = 0; i < transactionCount; i++) {
//             String accountNumber = accounts.get(random.nextInt(accounts.size())).accountNumber;
//             int depositAmount = random.nextInt(50000) + 1; // 1-50000 BDT
//             depositTransactions.add(new Transaction(accountNumber, depositAmount));
//             try {
//                 Thread.sleep(1000); // Sleep for 1 second before creating next transaction
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }
//     }
// }

// // Thread to generate withdraw transactions
// class WithdrawGenerationThread extends Thread {
//     List<Transaction> withdrawTransactions;
//     List<Account2> accounts;
//     int transactionCount;

//     public WithdrawGenerationThread(List<Transaction> withdrawTransactions, List<Account2> accounts, int count) {
//         this.withdrawTransactions = withdrawTransactions;
//         this.accounts = accounts;
//         this.transactionCount = count;
//     }

//     @Override
//     public void run() {
//         Random random = new Random();
//         for (int i = 0; i < transactionCount; i++) {
//             String accountNumber = accounts.get(random.nextInt(accounts.size())).accountNumber;
//             int withdrawAmount = random.nextInt(100000) + 1; // 1-100000 BDT
//             withdrawTransactions.add(new Transaction(accountNumber, withdrawAmount));
//             try {
//                 Thread.sleep(1000); // Sleep for 1 second before creating next transaction
//             } catch (InterruptedException e) {
//                 e.printStacktrace();
//             }
//         }
//     }
// }

// // Thread to process deposit transactions
// class DepositProcessingThread1 extends Thread {
//     List<Transaction> depositTransactions;
//     List<Account2> accounts;

//     public DepositProcessingThread1(List<Transaction> depositTransactions, List<Account2> accounts) {
//         this.depositTransactions = depositTransactions;
//         this.accounts = accounts;
//     }

//     @Override
//     public void run() {
//         while (true) {
//             Transaction transaction = null;
//             synchronized (depositTransactions) {
//                 if (!depositTransactions.isEmpty()) {
//                     transaction = depositTransactions.remove(0);
//                 }
//             }
//             if (transaction != null) {
//                 processTransaction(transaction);
//             } else {
//                 break; // No more transactions
//             }
//         }
//     }

//     private void processTransaction(Transaction transaction) {
//         Account2 account = accounts.stream()
//                                   .filter(acc -> acc.accountNumber.equals(transaction.accountNumber))
//                                   .findFirst().orElse(null);

//         if (account == null) {
//             System.out.println("Invalid account number: " + transaction.accountNumber);
//             return;
//         }

//         if (transaction.amount > account.maxTransactionLimit) {
//             System.out.println("Maximum DepositTransaction Limit Violated for Account2: " + transaction.accountNumber);
//             return;
//         }

//         account.balance.addAndGet(transaction.amount);
//         System.out.println("Deposited " + transaction.amount + " BDT to Account2: " + transaction.accountNumber);
//         try {
//             Thread.sleep(1000); // Wait 1 second before processing next transaction
//         } catch (InterruptedException e) {
//             e.printStack
