import java.util.*;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("All withdraw actions DONE!");
        System.out.println();
    }
}
