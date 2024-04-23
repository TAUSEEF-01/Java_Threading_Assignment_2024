import java.util.*;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("All deposit actions DONE!");
        System.out.println();
    }

}
