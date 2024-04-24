import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

// Custom Exception for Exceeding Maximum Transaction Limit
class MaxTransactionLimitExceededException2 extends Exception {
    public MaxTransactionLimitExceededException2(String message) {
        super(message); 
    }
}

// Account2 Class with Account2 Number, Name, Maximum Transaction Limit, and
// Balance
class Account2 {
    String accountHolderName;
    String accountNumber;
    int maximumTransactionLimit;
    int balance;

    public Account2(String accountHolderName, String accountNumber, int maximumTransactionLimit) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.maximumTransactionLimit = maximumTransactionLimit;
        this.balance = 0;
    }

    public synchronized void deposit(int amount) throws MaxTransactionLimitExceededException2 {
        if (amount > maximumTransactionLimit) {
            throw new MaxTransactionLimitExceededException2("Maximum DepositTransaction Limit Violated");
        }
        balance += amount;
    }

    public synchronized void withdraw(int amount) throws MaxTransactionLimitExceededException2 {
        if (amount > maximumTransactionLimit) {
            throw new MaxTransactionLimitExceededException2("Maximum WithdrawTransaction Limit Violated");
        }
        balance -= amount;
    }
}

// AccountGenerationThread2 - Generates 30 Random Accounts
class AccountGenerationThread2 extends Thread {
    List<Account2> accounts;

    public AccountGenerationThread2(List<Account2> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            String accountHolderName = "AccountHolder_" + i;
            String accountNumber = "AB" + String.format("%010d", random.nextInt(1000000000));
            int maximumTransactionLimit = random.nextInt(1000) + 1;
            accounts.add(new Account2(accountHolderName, accountNumber, maximumTransactionLimit));
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// DepositGenerationThread2 - Generates 500 Random Deposit Transactions
class DepositGenerationThread2 extends Thread {
    List<Account2> accounts;
    List<Map<String, Object>> depositTransactions;

    public DepositGenerationThread2(List<Account2> accounts, List<Map<String, Object>> depositTransactions) {
        this.accounts = accounts;
        this.depositTransactions = depositTransactions;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            Map<String, Object> transaction = new HashMap<>();
            int accountIndex = random.nextInt(accounts.size());
            transaction.put("accountNumber", accounts.get(accountIndex).accountNumber);
            transaction.put("depositAmount", random.nextInt(50000) + 1);
            synchronized (depositTransactions) {
                depositTransactions.add(transaction);
            }
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// WithdrawGenerationThread2 - Generates 500 Random Withdrawal Transactions
class WithdrawGenerationThread2 extends Thread {
    List<Account2> accounts;
    List<Map<String, Object>> withdrawTransactions;

    public WithdrawGenerationThread2(List<Account2> accounts, List<Map<String, Object>> withdrawTransactions) {
        this.accounts = accounts;
        this.withdrawTransactions = withdrawTransactions;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            Map<String, Object> transaction = new HashMap<>();
            int accountIndex = random.nextInt(accounts.size());
            transaction.put("accountNumber", accounts.get(accountIndex).accountNumber);
            transaction.put("withdrawAmount", random.nextInt(100000) + 1);
            synchronized (withdrawTransactions) {
                withdrawTransactions.add(transaction);
            }
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// DepositProcessingThread2 - Processes Deposit Transactions
class DepositProcessingThread2 extends Thread {
    List<Account2> accounts;
    List<Map<String, Object>> depositTransactions;
    int sleepTime;

    public DepositProcessingThread2(List<Account2> accounts, List<Map<String, Object>> depositTransactions,
            int sleepTime) {
        this.accounts = accounts;
        this.depositTransactions = depositTransactions;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (depositTransactions) {
                if (!depositTransactions.isEmpty()) {
                    Map<String, Object> transaction = depositTransactions.remove(0);
                    String accountNumber = (String) transaction.get("accountNumber");
                    int depositAmount = (int) transaction.get("depositAmount");

                    for (Account2 account : accounts) {
                        if (account.accountNumber.equals(accountNumber)) {
                            try {
                                account.deposit(depositAmount);
                            } catch (MaxTransactionLimitExceededException2 e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        }
                    }
                }
            }
            try {

                // Continuing from where the previous code snippet stopped
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// WithdrawProcessingThread2 - Processes Withdraw Transactions
class WithdrawProcessingThread2 extends Thread {
    List<Account2> accounts;
    List<Map<String, Object>> withdrawTransactions;
    int sleepTime;

    public WithdrawProcessingThread2(List<Account2> accounts, List<Map<String, Object>> withdrawTransactions,
            int sleepTime) {
        this.accounts = accounts;
        this.withdrawTransactions = withdrawTransactions;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (withdrawTransactions) {
                if (!withdrawTransactions.isEmpty()) {
                    Map<String, Object> transaction = withdrawTransactions.remove(0);
                    String accountNumber = (String) transaction.get("accountNumber");
                    int withdrawAmount = (int) transaction.get("withdrawAmount");

                    for (Account2 account : accounts) {
                        if (account.accountNumber.equals(accountNumber)) {
                            try {
                                account.withdraw(withdrawAmount);
                            } catch (MaxTransactionLimitExceededException2 e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        }
                    }
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

public class Roll_x {
    public static void main(String[] args) {
        // Shared data structures for accounts and transactions
        List<Account2> accounts = Collections.synchronizedList(new ArrayList<>());
        List<Map<String, Object>> depositTransactions = Collections.synchronizedList(new ArrayList<>());
        List<Map<String, Object>> withdrawTransactions = Collections.synchronizedList(new ArrayList<>());

        // Creating and starting the AccountGenerationThread2
        AccountGenerationThread2 accountGenThread = new AccountGenerationThread2(accounts);
        accountGenThread.start();

        try {
            // Wait for accounts to be generated
            accountGenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Creating and starting the DepositGenerationThread2 and
        // WithdrawGenerationThread2
        DepositGenerationThread2 depositGenThread = new DepositGenerationThread2(accounts, depositTransactions);
        WithdrawGenerationThread2 withdrawGenThread = new WithdrawGenerationThread2(accounts, withdrawTransactions);

        depositGenThread.start();
        withdrawGenThread.start();

        // Creating and starting the DepositProcessingThread1 and
        // DepositProcessingThread2
        DepositProcessingThread2 depositProcThread1 = new DepositProcessingThread2(accounts, depositTransactions, 1000);
        DepositProcessingThread2 depositProcThread2 = new DepositProcessingThread2(accounts, depositTransactions, 800);

        depositProcThread1.start();
        depositProcThread2.start();

        // Creating and starting the WithdrawProcessingThread1 and
        // WithdrawProcessingThread2
        WithdrawProcessingThread2 withdrawProcThread1 = new WithdrawProcessingThread2(accounts, withdrawTransactions,
                1000);
        WithdrawProcessingThread2 withdrawProcThread2 = new WithdrawProcessingThread2(accounts, withdrawTransactions,
                800);

        withdrawProcThread1.start();
        withdrawProcThread2.start();

        // Ensure that all threads complete before main exits
        try {
            depositGenThread.join();
            withdrawGenThread.join();
            depositProcThread1.join();
            depositProcThread2.join();
            withdrawProcThread1.join();
            withdrawProcThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
