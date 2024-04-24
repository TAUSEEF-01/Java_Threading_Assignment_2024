import java.util.*;

public class Roll_24 {

    public static Random random;
    public static int numberOfWorkerThreads;
    public static int sleepTime;

    public static void main(String[] args) {
        Map<Integer, Account> mac = new HashMap<>();

        AccountGenerationThread accountGenThread = new AccountGenerationThread(mac);
        System.out.println("AccountGenerationThread started!");
        accountGenThread.start();

        try {
            accountGenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] arrList_Deposit = new ArrayList[30];
        for (int i = 0; i < 30; i++) {
            arrList_Deposit[i] = new ArrayList<>();
        }

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] arrList_Withdraw = new ArrayList[30];
        for (int i = 0; i < 30; i++) {
            arrList_Withdraw[i] = new ArrayList<>();
        }

        DepositGenerationThread depositGenThread = new DepositGenerationThread(arrList_Deposit);
        WithdrawGenerationThread withdrawGenThread = new WithdrawGenerationThread(arrList_Withdraw);
        System.out.println("DepositGenerationThread started!");
        depositGenThread.start();
        System.out.println("WithdrawGenerationThread started!");
        withdrawGenThread.start();

        try {
            depositGenThread.join();
            withdrawGenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        random = new Random();
        numberOfWorkerThreads = 2 + random.nextInt(4);

        List<DepositProcessingThread> depositProcThreads = new ArrayList<>();
        for (int i = 1; i <= numberOfWorkerThreads; i++) {
            if (i % 2 == 0)
                sleepTime = 800;
            else
                sleepTime = 1000;

            DepositProcessingThread depositProcThread = new DepositProcessingThread(mac, arrList_Deposit, sleepTime, i);
            depositProcThreads.add(depositProcThread);
            System.out.println("DepositProcessingThread " + i + " started!");
            depositProcThread.start();
        }

        for (DepositProcessingThread thread : depositProcThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<WithdrawProcessingThread> withdrawProcThreads = new ArrayList<>();
        for (int i = 1; i <= numberOfWorkerThreads; i++) {
            if (i % 2 == 0)
                sleepTime = 800;
            else
                sleepTime = 1000;

            WithdrawProcessingThread withdrawProcThread = new WithdrawProcessingThread(mac, arrList_Withdraw, sleepTime,
                    i);
            withdrawProcThreads.add(withdrawProcThread);
            System.out.println("WithdrawProcessingThread " + i + " started!");
            withdrawProcThread.start();
        }

        for (WithdrawProcessingThread thread : withdrawProcThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Program END here!!!");
    }
}

class Account {
    public String name;
    public String accNumber;
    public int maxTransactionLimit;
    public int balance;

    public Account(String name, String accNumber, int maxTransactionLimit) {
        this.name = name;
        this.accNumber = accNumber;
        this.maxTransactionLimit = maxTransactionLimit;
        this.balance = 0;
    }

    public void deposit(int amount, int id) throws MaxDepositTransactionLimitException {

        try {
            if (amount > maxTransactionLimit) {
                throw new MaxDepositTransactionLimitException("Maximum DepositTransaction Limit Violated");
            }
            balance += amount;
            System.out.println("Money: " + amount + " added to the account!");
        } catch (MaxDepositTransactionLimitException e) {
            System.out.println(e + " from ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void withdraw(int amount, int id) throws MaxWithdrawTransactionLimitException {

        try {
            if (amount > maxTransactionLimit) {
                throw new MaxWithdrawTransactionLimitException("Maximum WithdrawTransaction Limit Violated");
            }
            balance -= amount;
            System.out.println("Money: " + amount + " withdrawn from the account!");
        } catch (MaxWithdrawTransactionLimitException e) {
            System.out.println(e + " from ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class AccountGenerationThread extends Thread {

    Map<Integer, Account> mac;
    RandomGenerator random;

    public AccountGenerationThread(Map<Integer, Account> mac) {
        this.mac = mac;
        this.random = new RandomGenerator();
    }

    @Override
    public synchronized void run() {
        System.out.println("Generating Accounts!");
        for (int i = 0; i < 30; i++) {
            System.out.println("Generating acc id: " + i + " Current Thread: " + Thread.currentThread());

            String accountHolderName = random.generateName();
            String accountNumber = random.generateAccountNumber();
            int maximumTransactionLimit = random.generateNumber(1001);

            Account acc = new Account(accountHolderName, accountNumber, maximumTransactionLimit);
            mac.put(i, acc);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Accounts Generated!");
        System.out.println();
    }
}

class DepositGenerationThread extends Thread {
    ArrayList<Integer>[] arrList;
    RandomGenerator random;

    public DepositGenerationThread(ArrayList<Integer>[] arrList) {
        this.arrList = arrList;
        this.random = new RandomGenerator();
    }

    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {

            int id = random.generateNumber(30);
            System.out
                    .println("Deposit no: " + (i + 1) + " " + "Deposited to ID: " + id + " " + Thread.currentThread());

            int DepositAmount = random.generateNumber(50001);
            arrList[id].add(DepositAmount);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("All deposit actions DONE!");
        System.out.println();
    }

}

class WithdrawGenerationThread extends Thread {
    ArrayList<Integer>[] arrList;
    RandomGenerator random;

    public WithdrawGenerationThread(ArrayList<Integer>[] arrList) {
        this.arrList = arrList;
        this.random = new RandomGenerator();
    }

    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {

            int id = random.generateNumber(30);
            System.out.println(
                    "Withdraw no: " + (i + 1) + " " + "Withdraw from ID: " + id + " " + Thread.currentThread());

            int WithdrawAmount = random.generateNumber(100001);
            arrList[id].add(WithdrawAmount);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("All withdraw actions DONE!");
        System.out.println();
    }
}

class DepositProcessingThread extends Thread {
    Map<Integer, Account> mac;
    ArrayList<Integer>[] arrList;
    RandomGenerator random;
    int id;
    int sleepTime;
    int threadNo;
    int processNo;

    public DepositProcessingThread(Map<Integer, Account> mac, ArrayList<Integer>[] arrList, int sleepTime,
            int threadNo) {
        this.mac = mac;
        this.arrList = arrList;
        this.sleepTime = sleepTime;
        this.random = new RandomGenerator();
        this.id = 0;
        this.threadNo = threadNo;
        processNo = 1;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (arrList) {
                if (!arrList[id].isEmpty()) {

                    int sz = arrList[id].size();
                    for (int i = 0; i < sz; i++) {
                        System.out
                                .println("ID: " + id + " " + "Current Thread: " + threadNo + " "
                                        + Thread.currentThread());
                        Account acc = mac.get(id);
                        int DepositAmount = arrList[id].get(i);

                        try {
                            acc.deposit(DepositAmount, id);
                        } catch (MaxDepositTransactionLimitException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < sz; i++) {
                        arrList[id].remove(0);
                    }

                    id++;

                } else {
                    id++;
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (id >= 30)
                break;
        }

        System.out.println("Deposit processing DONE!");
        System.out.println();
    }
}

class WithdrawProcessingThread extends Thread {
    Map<Integer, Account> mac;
    ArrayList<Integer>[] arrList;
    RandomGenerator random;
    int id;
    int sleepTime;
    int threadNo;
    int processNo;

    public WithdrawProcessingThread(Map<Integer, Account> mac, ArrayList<Integer>[] arrList, int sleepTime,
            int threadNo) {
        this.mac = mac;
        this.arrList = arrList;
        this.sleepTime = sleepTime;
        this.random = new RandomGenerator();
        this.id = 0;
        this.threadNo = threadNo;
        processNo = 1;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (arrList) {
                if (!arrList[id].isEmpty()) {

                    int sz = arrList[id].size();
                    for (int i = 0; i < sz; i++) {
                        System.out
                                .println("ID: " + id + " " + "Current Thread: " + threadNo + " "
                                        + Thread.currentThread());
                        Account acc = mac.get(id);
                        int WithdrawAmount = arrList[id].get(i);

                        try {
                            acc.withdraw(WithdrawAmount, id);
                        } catch (MaxWithdrawTransactionLimitException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < sz; i++) {
                        arrList[id].remove(0);
                    }

                    id++;

                } else {
                    id++;
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (id >= 30)
                break;
        }

        System.out.println("Withdraw processing DONE!");
        System.out.println();
    }
}

class RandomGenerator {
    private String allChars = "abcdefghijklmnopqrstuvwxyz";
    private Random rndm = new Random();

    public String generateName() {
        int n = rndm.nextInt(15);
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int randomIndex = rndm.nextInt(allChars.length());
            sb.append(allChars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(12);

        for (int i = 0; i < 2; i++) {
            int randomIndex = rndm.nextInt(allChars.length());
            sb.append(allChars.charAt(randomIndex));
        }

        for (int i = 0; i < 10; i++) {
            int randomDigit = rndm.nextInt(10);
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    public int generateNumber(int limit) {
        int value = rndm.nextInt(limit);

        return value;
    }

}

class MaxDepositTransactionLimitException extends Exception {
    public MaxDepositTransactionLimitException(String s) {
        super(s);
    }
}

class MaxWithdrawTransactionLimitException extends Exception {
    public MaxWithdrawTransactionLimitException(String s) {
        super(s);
    }
}
