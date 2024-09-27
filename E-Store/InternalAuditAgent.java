
import java.util.Random;

public class InternalAuditAgent implements Runnable {
    private int lastAudit;
    private final BankAccount account1;
    private final BankAccount account2;
    private final Random rand = new Random();

    int MAXSLEEP = 40000;
    public InternalAuditAgent(BankAccount account1, BankAccount account2) {
        this.lastAudit = 0;
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

            boolean account1Locked = false;
            boolean account2Locked = false;

            try {
                account1Locked = account1.getLock().tryLock();
                account2Locked = account2.getLock().tryLock();

                if (account1Locked && account2Locked) {
                    this.lastAudit = (BankAccount.getCurrentTransactionNumber() - 1) - lastAudit;
                    System.out.println("\n\n********************************************************************************************************************************************************\n\nInternal Bank Audit Beginning...\n\n\tThe total number of transactions since the last internal audit is: " + this.lastAudit + "\n");
                    System.out.println("\tINTERNAL BANK AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR " + account1.getName() + " TO BE: $" + account1.getBalance());
                    System.out.println("\tINTERNAL BANK AUDITOR FINDS CURRENT ACCOUNT BALANCE FOR " + account2.getName() + " TO BE: $" + account2.getBalance() + "\n\nInternal Bank Audit Complete.\n\n********************************************************************************************************************************************************\n\n");
                    this.lastAudit += (BankAccount.getCurrentTransactionNumber() - 1) - lastAudit;

                }

            } finally {
                if (account1Locked) {
                    account1.getLock().unlock();
                }
                if (account2Locked) {
                    account2.getLock().unlock();
                }
            }
        }
    }
}
