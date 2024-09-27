import java.util.Random;

public class DepositorAgent implements Runnable {
    private String name;
    private final BankAccount account1;
    private final BankAccount account2;
    private final Random rand = new Random();

    int MAXSLEEP = 14000;
    public DepositorAgent(BankAccount account1, BankAccount account2, int number) {
        this.name = "Agent DT" + number;
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

            int depositAmount = rand.nextInt(600) + 1;

            BankAccount selectedAccount = rand.nextInt(2) == 0 ? account1 : account2;

            selectedAccount.deposit(depositAmount, this.name);
        }
    }
}
