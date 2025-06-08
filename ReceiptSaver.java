import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReceiptSaver {
    public static void saveReceipt(String filePath, String musical, String selectedDate, String selectedTimeSlot, String receiptDetails, String totalCost) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\naomi\\Downloads\\New folder\\ReceiptSaver.txt", true))) {
            writer.println("Musical: " + musical);
            writer.println("Date: " + selectedDate);
            writer.println("Time Slot: " + selectedTimeSlot + System.lineSeparator());
            writer.println(receiptDetails);
            writer.println("Total Cost: " + totalCost);
            writer.println(); // Add a separator between transactions
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

