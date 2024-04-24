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
        }
    }
}
