import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LondonMusicalTicketSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("London Musical Ticket System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new FlowLayout());

            JButton bookButton = new JButton("View Musicals");
            JButton showSchedule = new JButton("Show Schedule");
            JButton viewButton = new JButton("View Tickets");
            JButton exitButton = new JButton("Exit");

            bookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Add your logic for booking tickets here
                    String[] musicals = {
                            "The Phantom of the Opera",
                            "Les Misérables",
                            "Wicked",
                            "Hamilton",
                            "Cats",
                            "The Lion King",
                            "Mamma Mia!",
                            "Matilda the Musical",
                            "Chicago",
                            "Rent"
                            // Add more musicals if needed
                    };

                    String selectedMusical = (String) JOptionPane.showInputDialog(
                            frame,
                            "Musical List:",
                            "View Musical List",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            musicals,
                            musicals[0]
                    );

                    if (selectedMusical != null) {
                        JOptionPane.showMessageDialog(frame, "Booking Tickets for: " + selectedMusical);
                    }
                }
            });

            showSchedule.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Instantiate and show the existing ShowScheduleFrame
                    ShowScheduleFrame scheduleFrame = new ShowScheduleFrame();
                    scheduleFrame.setVisible(true);
                }
            });

            viewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Add your logic for viewing tickets here
                    JOptionPane.showMessageDialog(frame, "Viewing Tickets");
                }
            });

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            frame.add(bookButton);
            frame.add(showSchedule);
            frame.add(viewButton);
            frame.add(exitButton);

            frame.setVisible(true);
        });
    }
}

class ShowScheduleFrame extends JFrame {
    public ShowScheduleFrame() {
        setTitle("Show Schedule");
        setSize(400, 200);
        // Add components or logic for the Show Schedule frame here
    }
}
