import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class TransactionLogger {

    public static void logTransaction(int amount, String type, String agentName, int transactionNumber) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York")).withFixedOffsetZone();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss 'EST'");
        String timestamp = now.format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            String amt = String.format("%.2f", (double)amount);
            writer.write(agentName + " issued " + type + " of $" + amt + " at: " + timestamp + "\tTransaction Number : " + transactionNumber);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
