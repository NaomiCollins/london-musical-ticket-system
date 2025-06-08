import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class BookMyTicketFrame extends JFrame {

    private JPanel seatPanel;
    private JPanel ticketPanel;
    private JPanel costPanel;
    private final Set<String> selectedSeats;
    private double ticketCost = 0.0;
    private String musical;
    private JButton addSeatButton;
    private JButton calculateTotalCostButton;
    private JTextField totalCostTextField;
    private boolean isFirstSeatSelected = false;
    private JTextArea receiptTextArea;
    private int ticketNumber = 1;
    private ShowScheduleFrame showScheduleFrame;
    private String selectedDate;

    public BookMyTicketFrame(String musical, String selectedDate, ShowScheduleFrame showScheduleFrame) {
        setTitle("Book My Ticket");
        setSize(1000, 400);
        setLayout(new GridLayout(1, 3));

        selectedSeats = new HashSet<>();
        this.showScheduleFrame = showScheduleFrame;
        this.selectedDate = selectedDate;

        createSeatPanel();
        createTicketPanel();
        createCostPanel();
        createCalculateTotalCostButton();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        this.musical = musical;
    }

    private void createSeatPanel() {
        seatPanel = new JPanel(new GridLayout(5, 6));
        seatPanel.setBorder(BorderFactory.createTitledBorder("Select Your Seat"));

        for (int i = 1; i <= 30; i++) {
            JButton seatButton = createSeatButton(String.valueOf(i));
            seatPanel.add(seatButton);
        }

        add(seatPanel);
    }

    private JButton createSeatButton(String seatNumber) {
        JButton seatButton = new JButton(seatNumber);
        seatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSeatSelection(seatButton.getText());
            }
        });
        return seatButton;
    }

    private void createTicketPanel() {
        ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.setBorder(BorderFactory.createTitledBorder("Select Ticket Type"));

        JPanel radioPanel = new JPanel(new GridLayout(3, 1));
        String[] ticketTypes = {"Adult", "Senior", "Student"};
        ButtonGroup ticketGroup = new ButtonGroup();

        for (String type : ticketTypes) {
            JRadioButton radioButton = new JRadioButton(type);
            ticketGroup.add(radioButton);
            radioPanel.add(radioButton);
        }

        addSeatButton = new JButton("Add Seat");
        addSeatButton.setEnabled(false);
        addSeatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAnotherSeat();
            }
        });

        ticketPanel.add(radioPanel, BorderLayout.CENTER);
        ticketPanel.add(addSeatButton, BorderLayout.SOUTH);

         JButton viewDetailsButton = new JButton("View Ticket Details");
    viewDetailsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Disable the button after it's clicked
            viewDetailsButton.setEnabled(false);

            // Generate receipt
            generateReceipt();
        }
    });

        ticketPanel.add(viewDetailsButton, BorderLayout.EAST);
        add(ticketPanel);
    }

   private void addAnotherSeat() {
    selectedSeats.clear();
    enableAllSeatButtons();
    addSeatButton.setEnabled(false);
    isFirstSeatSelected = false;
    ticketNumber = 1;
    generateReceipt();
    
    // Enable the "View Ticket Details" button
    enableViewDetailsButton(true);
}
   
   // Add this method to enable or disable the "View Ticket Details" button
private void enableViewDetailsButton(boolean enable) {
    Component[] components = ticketPanel.getComponents();
    for (Component component : components) {
        if (component instanceof JButton) {
            JButton viewDetailsButton = (JButton) component;
            if ("View Ticket Details".equals(viewDetailsButton.getText())) {
                viewDetailsButton.setEnabled(enable);
                break;
            }
        }
    }
}

    private void enableAllSeatButtons() {
        Component[] components = seatPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton seatButton = (JButton) component;
                seatButton.setEnabled(true);
            }
        }
    }

    private void toggleSeatSelection(String seat) {
        if (isFirstSeatSelected) {
            return;
        }

        selectedSeats.clear();
        selectedSeats.add(seat);

        String message = "Now, select the ticket type for Seat " + seat;
        JOptionPane.showMessageDialog(this, message, "Select Ticket Type", JOptionPane.INFORMATION_MESSAGE);

        updateSeatColor(seat);
        updateSeatButtons();

        isFirstSeatSelected = true;
    }

    private void updateSeatColor(String seat) {
        Component[] components = seatPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton seatButton = (JButton) component;
                String seatNumber = seatButton.getText();
                if (seatNumber.equals(seat)) {
                    seatButton.setBackground(Color.RED);
                    seatButton.setEnabled(false);
                }
            }
        }
    }

    private void updateSeatButtons() {
        Component[] components = seatPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton seatButton = (JButton) component;
                String seatNumber = seatButton.getText();
                if (!selectedSeats.contains(seatNumber)) {
                    seatButton.setEnabled(true);
                }
            }
        }
        addSeatButton.setEnabled(true);
    }

    private void createCostPanel() {
        costPanel = new JPanel(new BorderLayout());
        costPanel.setBorder(BorderFactory.createTitledBorder("Ticket Cost"));

        receiptTextArea = new JTextArea();
        receiptTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        costPanel.add(scrollPane, BorderLayout.CENTER);

        add(costPanel);
    }

    private void createCalculateTotalCostButton() {
        calculateTotalCostButton = new JButton("Calculate Total Cost");

        totalCostTextField = new JTextField();
        totalCostTextField.setEditable(false);

        calculateTotalCostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotalCost();
            }
        });

        JButton printReceiptButton = new JButton("Print My Receipt");
        printReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReceipt();
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(calculateTotalCostButton, BorderLayout.NORTH);
        buttonPanel.add(totalCostTextField, BorderLayout.CENTER);
        buttonPanel.add(printReceiptButton, BorderLayout.SOUTH);

        costPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void printReceipt() {
    // Check if showScheduleFrame is not null
    if (showScheduleFrame == null) {
        // Handle the case where showScheduleFrame is null
        JOptionPane.showMessageDialog(this, "Error: ShowScheduleFrame is null", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Gather details to pass to PrintMyReceiptFrame
    String musicalTitle = musical;
    String receiptDetails = receiptTextArea.getText();
    String totalCost = totalCostTextField.getText();

    // Get the time slot information from the ShowScheduleFrame
    String timeSlot = showScheduleFrame.getSelectedTimeSlot();

    // Check if timeSlot is not null
    if (timeSlot == null) {
        // Handle the case where timeSlot is null
        JOptionPane.showMessageDialog(this, "Error: Time slot is null", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Create an instance of PrintMyReceiptFrame
    PrintMyReceiptFrame printMyReceiptFrame = new PrintMyReceiptFrame(musicalTitle, "Receipt", selectedDate, timeSlot, receiptDetails, totalCost);

    // Dispose current frame
    dispose();
}


    private void calculateTotalCost() {
        double totalCost = 0.0;

        String[] lines = receiptTextArea.getText().split("\n");
        for (String line : lines) {
            if (line.startsWith("Cost for Seat")) {
                String costString = line.substring(line.lastIndexOf("$") + 1);
                double seatCost = Double.parseDouble(costString);
                totalCost += seatCost;
            }
        }

        totalCostTextField.setText("$" + totalCost);
    }

    private double getTicketTypeCost(String selectedTicketType) {
        double adultCost = 10.0;
        double seniorStudentCost = 8.0;

        return selectedTicketType.equals("Adult") ? adultCost : seniorStudentCost;
    }

    private String getSelectedTicketType() {
        for (Component component : ticketPanel.getComponents()) {
            if (component instanceof JPanel) {
                Component[] radioComponents = ((JPanel) component).getComponents();
                for (Component radioComponent : radioComponents) {
                    if (radioComponent instanceof JRadioButton) {
                        JRadioButton radioButton = (JRadioButton) radioComponent;
                        if (radioButton.isSelected()) {
                            return radioButton.getText();
                        }
                    }
                }
            }
        }
        return null;
    }

    private void generateReceipt() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a seat.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int startingTicketNumber = ticketNumber;

        StringBuilder receipt = new StringBuilder();

        for (String selectedSeat : selectedSeats) {
            receipt.append("Selected Seat: ").append(selectedSeat).append("\n");

            String selectedTicketType = getSelectedTicketType();
            if (selectedTicketType == null) {
                JOptionPane.showMessageDialog(this, "Please select a ticket type.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double ticketTypeCost = getTicketTypeCost(selectedTicketType);
            receipt.append("Ticket Type: ").append(selectedTicketType).append("\n");
            receipt.append("Ticket Cost per ").append(selectedTicketType).append(": $").append(ticketTypeCost).append("\n");
            receipt.append("Cost for Seat ").append(selectedSeat).append(": $").append(ticketTypeCost).append("\n\n");
        }

        ticketNumber = startingTicketNumber + 1;

        receiptTextArea.append(receipt.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookMyTicketFrame("Musical", "Selected Date", null));
    }
}