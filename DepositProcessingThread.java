import java.util.*;

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
                        System.out.println("Process No: " + processNo);
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
                        processNo++;
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
            }

            if (id >= 30)
                break;
        }

        System.out.println("Deposit processing DONE!");
        System.out.println();
    }
}
