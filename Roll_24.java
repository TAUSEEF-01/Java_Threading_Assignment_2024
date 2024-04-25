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

// import java.util.*;

// public class Roll_24 {
// public static void main(String[] args) {
// Map<Integer, Account> mac = new HashMap<>();

// AccountGenerationThread accountGenThread = new AccountGenerationThread(mac);
// System.out.println("AccountGenerationThread started!");
// accountGenThread.start();

// try {
// accountGenThread.join();
// } catch (InterruptedException e) {
// e.printStackTrace();
// }

// @SuppressWarnings("unchecked")
// ArrayList<Integer>[] arrList_Deposit = new ArrayList[30];
// for (int i = 0; i < 30; i++) {
// arrList_Deposit[i] = new ArrayList<>();
// }

// @SuppressWarnings("unchecked")
// ArrayList<Integer>[] arrList_Withdraw = new ArrayList[30];
// for (int i = 0; i < 30; i++) {
// arrList_Withdraw[i] = new ArrayList<>();
// }

// DepositGenerationThread depositGenThread = new
// DepositGenerationThread(arrList_Deposit);
// WithdrawGenerationThread withdrawGenThread = new
// WithdrawGenerationThread(arrList_Withdraw);
// System.out.println("DepositGenerationThread started!");
// depositGenThread.start();
// System.out.println("WithdrawGenerationThread started!");
// withdrawGenThread.start();

// try {
// depositGenThread.join();
// withdrawGenThread.join();
// } catch (InterruptedException e) {
// e.printStackTrace();
// }

// Random random = new Random();
// int numberOfWorkerThreads = 2 + random.nextInt(4);
// int sleepTime;

// List<DepositProcessingThread> depositProcThreads = new ArrayList<>();
// for (int i = 1; i <= numberOfWorkerThreads; i++) {

// if(i%2==0)
// sleepTime = 800;
// else
// sleepTime = 1000;

// DepositProcessingThread depositProcThread = new DepositProcessingThread(mac,
// arrList_Deposit, sleepTime, i);
// depositProcThreads.add(depositProcThread);
// System.out.println("DepositProcessingThread " + i + " started!");
// depositProcThread.start();
// }

// // Wait for all Deposit Processing Threads to complete
// for (DepositProcessingThread thread : depositProcThreads) {
// try {
// thread.join();
// } catch (InterruptedException e) {
// e.printStackTrace();
// }
// }

// // DepositProcessingThread depositProcThread1 = new
// DepositProcessingThread(mac, arrList_Deposit, 1000, 1);
// // DepositProcessingThread depositProcThread2 = new
// DepositProcessingThread(mac, arrList_Deposit, 800, 2);

// // System.out.println("DepositProcessingThread1 started! Loop no: " + i);
// // depositProcThread1.start();
// // System.out.println("DepositProcessingThread2 started! Loop no: " + i);
// // depositProcThread2.start();

// // try {
// // depositProcThread1.join();
// // depositProcThread2.join();
// // } catch (InterruptedException e) {
// // e.printStackTrace();
// // }

// // for (int i = 1; i <= 5; i++) {

// // DepositProcessingThread depositProcThread1 = new
// DepositProcessingThread(mac, arrList_Deposit, 1000, 1);
// // DepositProcessingThread depositProcThread2 = new
// DepositProcessingThread(mac, arrList_Deposit, 800, 2);

// // System.out.println("DepositProcessingThread1 started! Loop no: " + i);
// // depositProcThread1.start();
// // System.out.println("DepositProcessingThread2 started! Loop no: " + i);
// // depositProcThread2.start();

// // // try {
// // // depositProcThread1.join();
// // // depositProcThread2.join();
// // // } catch (InterruptedException e) {
// // // e.printStackTrace();
// // // }
// // }

// List<WithdrawProcessingThread> withdrawProcThreads = new ArrayList<>();
// for (int i = 1; i <= numberOfWorkerThreads; i++) {

// if(i%2==0)
// sleepTime = 800;
// else
// sleepTime = 1000;

// WithdrawProcessingThread withdrawProcThread = new
// WithdrawProcessingThread(mac, arrList_Withdraw, sleepTime, i);
// withdrawProcThreads.add(withdrawProcThread);
// System.out.println("WithdrawProcessingThread " + i + " started!");
// withdrawProcThread.start();
// }

// for (WithdrawProcessingThread thread : withdrawProcThreads) {
// try {
// thread.join();
// } catch (InterruptedException e) {
// e.printStackTrace();
// }
// }

// // WithdrawProcessingThread withdrawProcThread1 = new
// WithdrawProcessingThread(mac, arrList_Withdraw, 1000, 1);
// // WithdrawProcessingThread withdrawProcThread2 = new
// WithdrawProcessingThread(mac, arrList_Withdraw, 800, 2);

// // System.out.println("WithdrawProcessingThread1 started!");
// // withdrawProcThread1.start();
// // System.out.println("WithdrawProcessingThread2 started!");
// // withdrawProcThread2.start();

// // try {
// // withdrawProcThread1.join();
// // withdrawProcThread2.join();
// // } catch (InterruptedException e) {
// // e.printStackTrace();
// // }

// System.out.println("Program END here!!!");
// }
// }
