import java.util.*;

public class AccountGenerationThread extends Thread {

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
            System.out.println("Generating acc id: " + i +" Current Thread: " + Thread.currentThread());

            String accountHolderName = random.generateName();
            String accountNumber = random.generateAccountNumber();
            int maximumTransactionLimit = random.generateNumber(1001);

            Account acc = new Account(accountHolderName, accountNumber, maximumTransactionLimit);
            mac.put(i, acc);

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Accounts Generated!");
        System.out.println();
    }
}
