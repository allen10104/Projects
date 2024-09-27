import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private String name;
    private int balance;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition emptyAccount = lock.newCondition();

    private static final AtomicInteger transactionCounter = new AtomicInteger(1);
    public BankAccount(String name) {
        this.name = name;
        balance = 0;
    }

    public void deposit(int amount, String name) {
        lock.lock();
        try {
            balance += amount;
            System.out.println(name + " deposits $" + amount + " into " + this.name + "\t\t\t\t\t\t(+) " + this.name + " balance is $" + this.balance + "\t\t\t\t\t\t\t\t\t\t\t\t" + BankAccount.getNextTransactionNumber() + "\n");
            if (amount >= 450) {
                System.out.printf("\n\n***Flagged Transaction***%s Made A Deposit In Excess Of $%.2f USD - See Flagged Transaction Log\n\n", name, 450.00);
                TransactionLogger.logTransaction(amount, "deposit", name, BankAccount.getCurrentTransactionNumber() - 1);
            }

            emptyAccount.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(int amount, String agentName) throws InterruptedException {
        lock.lock();
        try {
            while (balance < amount) {
                System.out.println("\t\t\t\t" + agentName + " attempts to withdraw $" + amount + " from " + this.name + " (********) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!! Balance only $" + this.balance + "\n");
                emptyAccount.await();
            }
            balance -= amount;
            System.out.println("\t\t\t\t" + agentName + " withdraws $" + amount + " from " + this.name + "\t\t(-) " + this.name + " balance is $" + this.balance + "\t\t\t\t\t\t\t\t\t\t\t\t" + BankAccount.getNextTransactionNumber() + "\n");
            if (amount >= 90) {
                System.out.printf("\n\n***Flagged Transaction***%s Made A Withdrawal In Excess Of $%.2f USD - See Flagged Transaction Log\n\n", agentName, 90.00);
                TransactionLogger.logTransaction(amount, "withdrawal", agentName, BankAccount.getCurrentTransactionNumber() - 1);
            }
        } finally {
            lock.unlock();
        }
    }

    public void transfer(int amount, String transferName, BankAccount to){
        lock.lock();
        to.getLock().lock();
        this.balance -= amount;
        to.setBalance(amount);
        System.out.println("TRANSFER --> " + transferName + " transferring $" + amount + " from " + this.name + " to " + to.getName() + " - - " + this.getName() + " balance is now $" + this.getBalance() + "\t\t\t\t\t\t\t\t\t\t\t" + BankAccount.getNextTransactionNumber());
        System.out.println("TRANSFER COMPLETE --> Account " + to.getName() + " balance now $" + to.getBalance() + "\n");

        lock.unlock();
        to.getLock().unlock();
    }

    public static int getNextTransactionNumber() {
        return transactionCounter.getAndIncrement();
    }
    public static int getCurrentTransactionNumber() {
        return transactionCounter.get();
    }

    public void setBalance(int amount){
        this.balance += amount;
    }

    public int getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public String getName(){
        return this.name;
    }

    public Lock getLock() {
        return this.lock;
    }
}
