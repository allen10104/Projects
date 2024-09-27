import java.util.Random;

public class WithdrawalAgent implements Runnable {
    private String name;
    private final BankAccount account1;
    private final BankAccount account2;
    private final Random rand = new Random();

    int MAXSLEEP = 4000;
    public WithdrawalAgent(BankAccount account1, BankAccount account2, int number) {
        this.name = "Agent WT" + number;
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(rand.nextInt(MAXSLEEP));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int withdrawalAmount = rand.nextInt(99) + 1;

            BankAccount selectedAccount = rand.nextInt(2) == 0 ? account1 : account2;


            try {
                selectedAccount.withdraw(withdrawalAmount, this.name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
