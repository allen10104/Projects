import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("***    SIMULATION BEGINS...\n");
        System.out.println("Deposit Agents\t\t\t\tWithdrawal Agents\t\t\t\tBalances\t\t\t\t\t\t\t\t\t\t\t\tTransaction Number\n--------------\t\t\t\t-----------------\t\t\t\t--------\t\t\t\t\t\t\t\t\t\t\t\t------------------");

        BankAccount account1 = new BankAccount("JA-1");
        BankAccount account2 = new BankAccount("JA-2");


        ExecutorService executor = Executors.newFixedThreadPool(19);

        for (int i = 0; i < 5; i++) {
            executor.submit(new DepositorAgent(account1, account2, i + 1));
        }


        for (int i = 0; i < 10; i++) {
            executor.submit(new WithdrawalAgent(account1, account2,i + 1));
        }


        for (int i = 0; i < 2; i++) {
            executor.submit(new TransferAgent(account1, account2, i + 1));
        }


        executor.submit(new InternalAuditAgent(account1, account2));


        executor.submit(new TreasuryAgent(account1, account2));

        executor.shutdown();
    }
}
