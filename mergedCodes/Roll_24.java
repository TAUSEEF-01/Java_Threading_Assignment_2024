import java.util.*;

public class Roll_24 {

    public static Random random;
    public static int numberOfWorkerThreads;
    public static int sleepTime;

    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        Map<Integer, Account> mac = new HashMap<>();

        Boolean running = true;
        while (running == true) {
            running = mainLoop(sc, running, mac);
        }
    }

    public static boolean mainLoop(Scanner sc, boolean running, Map<Integer, Account> mac) {
        clearScreen();
        System.out.println("Enter choice: ");
        System.out.println("1. Generate random information");
        System.out.println("2. Show information");
        System.out.println("3. Show all information");
        System.out.println("3. Type any number and hit enter to EXIT!");
        System.out.println();
        System.out.print("Your choice: ");
        int n;

        try {
            n = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid choice! " + e);
            running = false;
            return running;
        }

        if (mac.isEmpty() && (n == 2 || n == 3)) {
            clearScreen();
            System.out.println("Generate random information first!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return running;
        }

        int id;

        clearScreen();

        if (n == 1) {
            InfoGenerate(sc, mac);
            clearScreen();
        } else if (n == 2) {
            System.out.print("Enter person id: ");
            id = sc.nextInt();
            show_info(sc, mac, id);
            clearScreen();
        } else if (n == 3) {
            show_all_info(sc, mac);
            clearScreen();
        } else {
            running = false;
            return running;
        }

        System.out.println();

        clearScreen();
        return running;
    }

    public static void InfoGenerate(Scanner sc, Map<Integer, Account> mac) {

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

            Deposit_Processing_Thread depositProcThread1 = new Deposit_Processing_Thread(mac, arrList_Deposit,
                    1000,
                    i);
            Deposit_Processing_Thread depositProcThread2 = new Deposit_Processing_Thread(mac, arrList_Deposit,
                    800,
                    i);

            depositProcThreads.add(depositProcThread1);
            depositProcThreads.add(depositProcThread2);
            System.out.println("Deposit_Processing_Thread " + i + " started!");
            depositProcThread1.start();
            depositProcThread2.start();
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

            Withdraw_Processing_Thread withdrawProcThread1 = new Withdraw_Processing_Thread(mac, arrList_Withdraw,
                    1000,
                    i);

            Withdraw_Processing_Thread withdrawProcThread2 = new Withdraw_Processing_Thread(mac, arrList_Withdraw,
                    800,
                    i);

            withdrawProcThreads.add(withdrawProcThread1);
            withdrawProcThreads.add(withdrawProcThread2);
            System.out.println("Withdraw_Processing_Thread " + i + " started!");
            withdrawProcThread1.start();
            withdrawProcThread2.start();
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

        System.out.println();
        System.out.println("Random information generated!");

        while (true) {
            System.out.println("Press 0 to return.");
            int press = sc.nextInt();
            if (press == 0) {
                clearScreen();
                return;
            }
        }
    }

    public static void show_info(Scanner sc, Map<Integer, Account> mac, int id) {
        clearScreen();
        if (id >= 30) {
            System.out.println("Invalid ID!");
            System.out.println();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return;
        }

        Account s = mac.get(id);
        s.printInfo();

        while (true) {
            System.out.println("Press 0 to return.");
            int press = sc.nextInt();
            if (press == 0) {
                clearScreen();
                return;
            }
        }
    }

    public static void show_all_info(Scanner sc, Map<Integer, Account> mac) {
        clearScreen();
        Account s[] = new Account[30];

        for (int i = 0; i < 30; i++) {
            s[i] = mac.get(i);
            System.out.println("ID: " + i);
            s[i].printInfo();
            System.out.println();
        }

        while (true) {
            System.out.println("Press 0 to return.");
            int press = sc.nextInt();
            if (press == 0) {
                clearScreen();
                return;
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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

    public void printInfo() {
        System.out.println("Name: " + this.name);
        System.out.println("Account number: " + this.accNumber);
        System.out.println("Max transaction limit: " + this.maxTransactionLimit);
        System.out.println("Balance: " + this.balance);
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

class Withdraw_Generation_Thread extends Thread {
    ArrayList<Integer>[] arrList;
    RandomGenerator random;

    public Withdraw_Generation_Thread(ArrayList<Integer>[] arrList) {
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

                if (id >= 30)
                    break;

            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

                if (id >= 30)
                    break;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
