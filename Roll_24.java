import java.util.*;

public class Roll_24 {
    public static void main(String[] args) {
        Map<Integer, Account> mac = new HashMap<>();

        AccountGenerationThread accountGenThread = new AccountGenerationThread(mac);
        System.out.println("AccountGenerationThread started!");
        accountGenThread.start();

        try {
            accountGenThread.join();
        } catch (InterruptedException e) {
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
        }

        DepositProcessingThread depositProcThread1 = new DepositProcessingThread(mac, arrList_Deposit, 1000, 1);
        DepositProcessingThread depositProcThread2 = new DepositProcessingThread(mac, arrList_Deposit, 800, 2);
        System.out.println("DepositProcessingThread1 started!");
        depositProcThread1.start();
        System.out.println("DepositProcessingThread2 started!");
        depositProcThread2.start();

        try {
            depositProcThread1.join();
            depositProcThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WithdrawProcessingThread withdrawProcThread1 = new WithdrawProcessingThread(mac, arrList_Withdraw, 1000, 1);
        WithdrawProcessingThread withdrawProcThread2 = new WithdrawProcessingThread(mac, arrList_Withdraw, 800, 2);
        System.out.println("WithdrawProcessingThread1 started!");
        withdrawProcThread1.start();
        System.out.println("WithdrawProcessingThread2 started!");
        withdrawProcThread2.start();

        try {
            withdrawProcThread1.join();
            withdrawProcThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // try {
        // depositGenThread.join();
        // withdrawGenThread.join();
        // depositProcThread1.join();
        // depositProcThread2.join();
        // withdrawProcThread1.join();
        // withdrawProcThread2.join();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        System.out.println("Program END'S here!!!");
    }
}