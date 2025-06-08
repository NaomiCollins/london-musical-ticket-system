import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JDialog;

public class ShowScheduleFrame extends JFrame {

    private final java.util.List<JDialog> dialogs = new ArrayList<>();
    private final Map<String, String> musicalImages = createMusicalImageMap();
    private final Map<String, MusicalDetails> musicalDetailsMap = createMusicalDetailsMap();
    private String selectedTimeSlot;
    private JCalendar jCalendar;


     public String getSelectedTimeSlot() {
        return selectedTimeSlot;
    }
     
    public ShowScheduleFrame() {
        setTitle("Show Schedule");
        setSize(800, 600);
        setLayout(new BorderLayout());

        createMusicalButtons();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMusicalButtons() {
        JPanel musicalPanel = new JPanel(new GridLayout(5, 2, 10, 10));

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
        };

        for (String musical : musicals) {
            JButton musicalButton = new JButton(musical);
            musicalButton.addActionListener(e -> showMusicalDetails(musical));
            musicalPanel.add(musicalButton);
        }

        add(musicalPanel, BorderLayout.CENTER);
    }

    private void showMusicalDetails(String musical) {
        JDialog dialog = new JDialog(this, "Details for " + musical, true);
        dialogs.add(dialog);
        dialog.setSize(700, 600);
        dialog.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());

        JPanel imagePanel = createImagePanel(musical);
        detailsPanel.add(imagePanel, BorderLayout.WEST);

        JPanel musicalDetailsPanel = createMusicalDetailsPanel(musical);
        detailsPanel.add(musicalDetailsPanel, BorderLayout.SOUTH);

        JPanel calendarPanel = createCalendarPanel(musical, dialog);
        detailsPanel.add(calendarPanel, BorderLayout.CENTER);

        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createImagePanel(String musical) {
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String imagePath = musicalImages.get(musical);
        if (imagePath != null) {
            ImageIcon musicalImage = new ImageIcon(imagePath);
            Image scaledImage = musicalImage.getImage().getScaledInstance(250, 300, Image.SCALE_SMOOTH);
            ImageIcon scaledMusicalImage = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(scaledMusicalImage);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.TOP);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        }
        return imagePanel;
    }

    private JPanel createMusicalDetailsPanel(String musical) {
        JPanel musicalDetailsPanel = new JPanel();
        musicalDetailsPanel.setLayout(new BoxLayout(musicalDetailsPanel, BoxLayout.PAGE_AXIS));
        musicalDetailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        MusicalDetails details = musicalDetailsMap.get(musical);
        if (details != null) {
            JLabel runtimeLabel = new JLabel("Runtime: " + details.getRuntime());
            JLabel genreLabel = new JLabel("Genre: " + details.getGenre());
            JLabel venueLabel = new JLabel("Venue: " + details.getVenue());
            JLabel openingDateLabel = new JLabel("Opening Date: " + details.getOpeningDate());
            JLabel ageRatingLabel = new JLabel("Age Rating: " + details.getAgeRating());

            musicalDetailsPanel.add(new JLabel("Details for " + musical));
            musicalDetailsPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            musicalDetailsPanel.add(runtimeLabel);
            musicalDetailsPanel.add(genreLabel);
            musicalDetailsPanel.add(venueLabel);
            musicalDetailsPanel.add(openingDateLabel);
            musicalDetailsPanel.add(ageRatingLabel);
        }
        return musicalDetailsPanel;
    }

  private JPanel createCalendarPanel(String musical, JDialog dialog) {
    JPanel calendarPanel = new JPanel();
    calendarPanel.setLayout(new BorderLayout());
    calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Add label above the calendar
    JLabel pickDateLabel = new JLabel("Pick Your Date:");
    pickDateLabel.setHorizontalAlignment(JLabel.CENTER);
    calendarPanel.add(pickDateLabel, BorderLayout.NORTH);

    jCalendar = new JCalendar();
    jCalendar.setMinSelectableDate(new Date());
    calendarPanel.add(jCalendar, BorderLayout.CENTER);

    JButton selectDateButton = new JButton("Select Date");
    selectDateButton.addActionListener(e -> {
        Date selectedDate = jCalendar.getDate();
        showSelectedDate(selectedDate, musical, dialog);
    });
    calendarPanel.add(selectDateButton, BorderLayout.SOUTH);

    JPanel timeSlotPanel = new JPanel();
    timeSlotPanel.setLayout(new FlowLayout());

    JLabel chooseSlotLabel = new JLabel("Choose Your Slot:");

    JButton timeSlot1Button = new JButton("5:30 PM");
    timeSlot1Button.addActionListener(e -> handleTimeSlotSelection("5:30 PM", musical, dialog));

    JButton timeSlot2Button = new JButton("8:30 PM");
    timeSlot2Button.addActionListener(e -> handleTimeSlotSelection("8:30 PM", musical, dialog));

    timeSlotPanel.add(chooseSlotLabel);
    timeSlotPanel.add(timeSlot1Button);
    timeSlotPanel.add(timeSlot2Button);

    calendarPanel.add(timeSlotPanel, BorderLayout.SOUTH);

    return calendarPanel;
}



    private void handleTimeSlotSelection(String selectedTimeSlot, String musical, JDialog dialog) {
    this.selectedTimeSlot = selectedTimeSlot;  // Store the selected time slot
    Date selectedDate = jCalendar.getDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = dateFormat.format(selectedDate);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            dialog.dispose();
            dialogs.remove(dialog);
        });

        JButton pickSeatButton = new JButton("Pick Your Seat");
        pickSeatButton.addActionListener(e -> {
            BookMyTicketFrame bookMyTicketFrame = new BookMyTicketFrame(musical, formattedDate, this);
            setVisible(false);
            dialog.dispose();
            dialogs.remove(dialog);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(pickSeatButton);

        Object[] message = {
                "Selected Date and Time for " + musical + ": " + formattedDate + " " + selectedTimeSlot,
                buttonPanel
        };

        JOptionPane.showOptionDialog(
                this,
                message,
                "Confirmation",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{},
                null);
    }

    private void showSelectedDate(Date selectedDate, String musical, JDialog dialog) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(selectedDate);

        Object[] options = {"OK", "Pick Your Seat"};
        int result = JOptionPane.showOptionDialog(
                this,
                "Selected Date for " + musical + ": " + formattedDate,
                "Confirmation",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == 1) {
            BookMyTicketFrame bookMyTicketFrame = new BookMyTicketFrame(musical, formattedDate, this);
            setVisible(false);
            dialog.dispose();
            dialogs.remove(dialog);
        }
    }

    private Map<String, String> createMusicalImageMap() {
        Map<String, String> imageMap = new HashMap<>();
        imageMap.put("The Phantom of the Opera", "C:\\COMP1618\\Week01\\PHA23_Q2_017_TTG_480x720_AW.jpg");
        imageMap.put("Les Misérables", "C:\\COMP1618\\Week01\\LesMiz1080x1080.jpg");
        imageMap.put("Wicked", "C:\\COMP1618\\Week01\\MV5BNjczYjBhMTctYTA3Yy00NTgyLWFkZWQtZjQwYTRkMDc1YTc1XkEyXkFqcGdeQXVyNTk5NTQzNDI@._V1_.jpg");
        imageMap.put("Hamilton", "C:\\COMP1618\\Week01\\MV5BNjViNWRjYWEtZTI0NC00N2E3LTk0NGQtMjY4NTM3OGNkZjY0XkEyXkFqcGdeQXVyMjUxMTY3ODM@._V1_.jpg");
        imageMap.put("Cats", "C:\\COMP1618\\Week01\\MV5BNjRlNTY3MTAtOTViMS00ZjE5LTkwZGItMGYwNGQwMjg2NTEwXkEyXkFqcGdeQXVyNjg2NjQwMDQ@._V1_.jpg");
        imageMap.put("Mamma Mia!", "C:\\COMP1618\\Week01\\-mm-_ttg_600x900.jpg");
        imageMap.put("The Lion King", "C:\\COMP1618\\Week01\\TLK_Visual2_KV_querformat_rgb_300dpi_Giraffes.jpg");
        imageMap.put("Matilda the Musical", "C:\\COMP1618\\Week01\\product.437.jpg");
        imageMap.put("Chicago", "C:\\COMP1618\\Week01\\dbg69go-cd757a5c-83b8-4ed8-b523-9969996bdeb5.png");
        imageMap.put("Rent", "C:\\COMP1618\\Week01\\a2f1b7c0-4fa9-479b-9c29-533198ebfded.jpg");
        return imageMap;
    }

    private Map<String, MusicalDetails> createMusicalDetailsMap() {
        Map<String, MusicalDetails> detailsMap = new HashMap<>();
        detailsMap.put("The Phantom of the Opera", new MusicalDetails("2 hours and 30 minutes", "Musical Drama", "Her Majesty's Theatre", "January 26, 1988", "All ages"));
        detailsMap.put("Les Misérables", new MusicalDetails("3 hours", "Musical Epic", "Queen's Theatre", "October 8, 1985", "12+"));
        detailsMap.put("Wicked", new MusicalDetails("2 hours and 45 minutes", "Fantasy Musical", "Apollo Victoria Theatre", "September 27, 2006", "8+"));
        detailsMap.put("Hamilton", new MusicalDetails("2 hours and 55 minutes", "Historical Musical", "Victoria Palace Theatre", "December 21, 2017", "10+"));
        detailsMap.put("Cats", new MusicalDetails("2 hours and 20 minutes", "Jellicle Musical", "Gillian Lynne Theatre", "May 11, 1981", "All ages"));
        detailsMap.put("The Lion King", new MusicalDetails("2 hours and 30 minutes", "Disney Theatrical", "Lyceum Theatre", "October 19, 1999", "6+"));
        detailsMap.put("Mamma Mia!", new MusicalDetails("2 hours and 20 minutes", "Jukebox Musical", "Novello Theatre", "April 6, 1999", "8+"));
        detailsMap.put("Matilda the Musical", new MusicalDetails("2 hours and 40 minutes", "Family Musical", "Cambridge Theatre", "November 24, 2011", "6+"));
        detailsMap.put("Chicago", new MusicalDetails("2 hours and 30 minutes", "Jazz Musical", "Phoenix Theatre", "November 18, 1997", "12+"));
        detailsMap.put("Rent", new MusicalDetails("2 hours and 40 minutes", "Rock Musical", "Shaftesbury Theatre", "April 12, 1998", "14+"));
        return detailsMap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ShowScheduleFrame::new);
    }

    private static class MusicalDetails {
        private final String runtime;
        private final String genre;
        private final String venue;
        private final String openingDate;
        private final String ageRating;

        public MusicalDetails(String runtime, String genre, String venue, String openingDate, String ageRating) {
            this.runtime = runtime;
            this.genre = genre;
            this.venue = venue;
            this.openingDate = openingDate;
            this.ageRating = ageRating;
        }

        public String getRuntime() {
            return runtime;
        }

        public String getGenre() {
            return genre;
        }

        public String getVenue() {
            return venue;
        }

        public String getOpeningDate() {
            return openingDate;
        }

        public String getAgeRating() {
            return ageRating;
        }
    }
}
