import javax.swing.*;
import java.awt.*;

public class PrintMyReceiptFrame extends JFrame {
    private JTextArea receiptTextArea;

    public PrintMyReceiptFrame(String musical, String title, String selectedDate, String selectedTimeSlot, String receiptDetails, String totalCost) {
        setTitle(title);
        setSize(400, 300);

        receiptTextArea = new JTextArea();
        receiptTextArea.setEditable(false);
        receiptTextArea.append("Musical: " + musical + "\n");
        receiptTextArea.append("Date: " + selectedDate + "\n");
        receiptTextArea.append("Time Slot: " + selectedTimeSlot + "\n\n");
        receiptTextArea.append(receiptDetails);
        receiptTextArea.append("Total Cost: " + totalCost);

        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        add(scrollPane);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Save the receipt when the frame is created
        saveReceiptToFile(musical, selectedDate, selectedTimeSlot, receiptDetails, totalCost);
    }

    private void saveReceiptToFile(String musical, String selectedDate, String selectedTimeSlot, String receiptDetails, String totalCost) {
        ReceiptSaver.saveReceipt("receipts.txt", musical, selectedDate, selectedTimeSlot, receiptDetails, totalCost);
    }

    public static void main(String[] args) {
        // Example usage:
        SwingUtilities.invokeLater(() ->
                new PrintMyReceiptFrame(
                        "Example Musical",
                        "Frame Title",
                        "2023-12-31",
                        "Selected Time Slot", // Provide the selected time slot here
                        "Receipt details go here...",
                        "$50.00"
                )
        );
    }
}


