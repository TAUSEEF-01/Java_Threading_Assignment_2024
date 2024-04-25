import java.util.*;

public class Roll_24 {

    public static Random random;
    public static int numberOfWorkerThreads;
    public static int sleepTime;

    public static void main(String[] args) {
        Map<Integer, Account> mac = new HashMap<>();

        Account_Generation_Thread accountGenThread = new Account_Generation_Thread(mac);
        System.out.println("Account_Generation_Thread started!");
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

        Deposit_Generation_Thread depositGenThread = new Deposit_Generation_Thread(arrList_Deposit);
        Withdraw_Generation_Thread withdrawGenThread = new Withdraw_Generation_Thread(arrList_Withdraw);
        System.out.println("Deposit_Generation_Thread started!");
        depositGenThread.start();
        System.out.println("Withdraw_Generation_Thread started!");
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

        List<Deposit_Processing_Thread> depositProcThreads = new ArrayList<>();
        for (int i = 1; i <= numberOfWorkerThreads; i++) {
            if (i % 2 == 0)
                sleepTime = 800;
            else
                sleepTime = 1000;

            Deposit_Processing_Thread depositProcThread = new Deposit_Processing_Thread(mac, arrList_Deposit, sleepTime,
                    i);
            depositProcThreads.add(depositProcThread);
            System.out.println("Deposit_Processing_Thread " + i + " started!");
            depositProcThread.start();
        }

        for (Deposit_Processing_Thread thread : depositProcThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Withdraw_Processing_Thread> withdrawProcThreads = new ArrayList<>();
        for (int i = 1; i <= numberOfWorkerThreads; i++) {
            if (i % 2 == 0)
                sleepTime = 800;
            else
                sleepTime = 1000;

            Withdraw_Processing_Thread withdrawProcThread = new Withdraw_Processing_Thread(mac, arrList_Withdraw,
                    sleepTime,
                    i);
            withdrawProcThreads.add(withdrawProcThread);
            System.out.println("Withdraw_Processing_Thread " + i + " started!");
            withdrawProcThread.start();
        }

        for (Withdraw_Processing_Thread thread : withdrawProcThreads) {
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

    public void deposit(int amount, int id) throws Max_Deposit_Transaction_Limit_Exception {

        try {
            if (amount > maxTransactionLimit) {
                throw new Max_Deposit_Transaction_Limit_Exception("Maximum DepositTransaction Limit Violated");
            }
            balance += amount;
            System.out.println("Money: " + amount + " added to the account!");
        } catch (Max_Deposit_Transaction_Limit_Exception e) {
            System.out.println(e + " from ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void withdraw(int amount, int id) throws Max_Withdraw_Transaction_Limit_Exception {

        try {
            if (amount > maxTransactionLimit) {
                throw new Max_Withdraw_Transaction_Limit_Exception("Maximum WithdrawTransaction Limit Violated");
            }
            balance -= amount;
            System.out.println("Money: " + amount + " withdrawn from the account!");
        } catch (Max_Withdraw_Transaction_Limit_Exception e) {
            System.out.println(e + " from ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Account_Generation_Thread extends Thread {

    Map<Integer, Account> mac;
    RandomGenerator random;

    public Account_Generation_Thread(Map<Integer, Account> mac) {
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

class Deposit_Generation_Thread extends Thread {
    ArrayList<Integer>[] arrList;
    RandomGenerator random;

    public Deposit_Generation_Thread(ArrayList<Integer>[] arrList) {
        this.arrList = arrList;
        this.random = new RandomGenerator();
    }

    @Override
    public void run() {
        for (int i = 0; i < 13; i++) {

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

class Withdraw_Generation_Thread extends Thread {
    ArrayList<Integer>[] arrList;
    RandomGenerator random;

    public Withdraw_Generation_Thread(ArrayList<Integer>[] arrList) {
        this.arrList = arrList;
        this.random = new RandomGenerator();
    }

    @Override
    public void run() {
        for (int i = 0; i < 13; i++) {

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

class Deposit_Processing_Thread extends Thread {
    Map<Integer, Account> mac;
    ArrayList<Integer>[] arrList;
    RandomGenerator random;
    int id;
    int sleepTime;
    int threadNo;
    int processNo;

    public Deposit_Processing_Thread(Map<Integer, Account> mac, ArrayList<Integer>[] arrList, int sleepTime,
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
                        } catch (Max_Deposit_Transaction_Limit_Exception e) {
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

class Withdraw_Processing_Thread extends Thread {
    Map<Integer, Account> mac;
    ArrayList<Integer>[] arrList;
    RandomGenerator random;
    int id;
    int sleepTime;
    int threadNo;
    int processNo;

    public Withdraw_Processing_Thread(Map<Integer, Account> mac, ArrayList<Integer>[] arrList, int sleepTime,
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
                        } catch (Max_Withdraw_Transaction_Limit_Exception e) {
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

class Max_Deposit_Transaction_Limit_Exception extends Exception {
    public Max_Deposit_Transaction_Limit_Exception(String s) {
        super(s);
    }
}

class Max_Withdraw_Transaction_Limit_Exception extends Exception {
    public Max_Withdraw_Transaction_Limit_Exception(String s) {
        super(s);
    }
}
