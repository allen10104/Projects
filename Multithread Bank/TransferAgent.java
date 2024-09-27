import java.util.Random;

public class TransferAgent implements Runnable {
    String name;
    private BankAccount withdrawAccount;
    private BankAccount transferAccount;
    private final Random rand = new Random();

    int MAXSLEEP = 20000;

    public TransferAgent(BankAccount account1, BankAccount account2, int number) {
        this.name = "Agent TR" + number;
        this.withdrawAccount = account1;
        this.transferAccount = account2;
    }

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(rand.nextInt(MAXSLEEP));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int random = rand.nextInt(2) + 1;

            int transferAmount = rand.nextInt(99) + 1;


            boolean withdrawAccountLock = false;
            boolean transferAccountLock = false;

            try {
                withdrawAccountLock = withdrawAccount.getLock().tryLock();
                transferAccountLock  = transferAccount.getLock().tryLock();

                if (withdrawAccountLock && transferAccountLock) {
                    if (withdrawAccount.getBalance() >= transferAmount && random == 1)
                        withdrawAccount.transfer(transferAmount, this.name, transferAccount);
                    else if(transferAccount.getBalance() >= transferAmount && random != 1)
                        transferAccount.transfer(transferAmount, this.name, withdrawAccount);
                }
            } finally {
                if (withdrawAccountLock) {
                    withdrawAccount.getLock().unlock();
                }
                if (transferAccountLock) {
                    transferAccount.getLock().unlock();
                }
            }
        }
    }
}
