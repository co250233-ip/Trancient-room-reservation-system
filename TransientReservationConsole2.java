import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// ABSTRACTION
// PURPOSE: Room is an abstract blueprint for different room types.
// It cannot be instantiated directly; subclasses implement getRoomType().
abstract class Room {
    private String roomNumber;
    private double pricePer24Hours;
    private int maxAdults;
    private int maxChildren;
    private boolean isAvailable;

    public Room(String roomNumber, double pricePer24Hours,
                int maxAdults, int maxChildren) {
        this.roomNumber = roomNumber;
        this.pricePer24Hours = pricePer24Hours;
        this.maxAdults = maxAdults;
        this.maxChildren = maxChildren;
        this.isAvailable = true;
    }

    public String getRoomNumber() { return roomNumber; }
    public double getPricePer24Hours() { return pricePer24Hours; }
    public int getMaxAdults() { return maxAdults; }
    public int getMaxChildren() { return maxChildren; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    // ABSTRACTION
    // PURPOSE: Requires each Room subclass to define its room type.
    public abstract String getRoomType();
}

// StandardRoom (Inheritance)
class StandardRoom extends Room {
    public StandardRoom(String roomNumber) {
        super(roomNumber, 900.0, 2, 1);
    }

    // POLYMORPHISM (METHOD OVERRIDING)
    // PURPOSE: Supplies StandardRoom's implementation of getRoomType().
    @Override
    public String getRoomType() {
        return "Standard Room";
    }
}

// Hotel Class (Composition)
// PURPOSE: The hotel creates and owns its room objects
class Hotel {
    private List<Room> rooms = new ArrayList<>();

    public Hotel() {
        // Automatically create all 13 Standard Rooms
        for (int i = 1; i <= 13; i++) {
            rooms.add(new StandardRoom(String.valueOf(i)));
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }
}

// Guest Class (Encapsulation)
class Guest {
    private String name;
    private String contactNumber;
    private int numberOfAdults;
    private int numberOfChildren;

    public Guest(String name, String contactNumber,
                 int numberOfAdults, int numberOfChildren) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public int getNumberOfAdults() { return numberOfAdults; }
    public int getNumberOfChildren() { return numberOfChildren; }
}

// Reservation Class (Encapsulation)
class Reservation {
    // Aggregation
    // PURPOSE: Reservation uses guest and room objects created separately and these objects can exist independently
    private Guest guest;
    private Room room;
    private String checkInDate;
    private String checkOutDate;
    private String timeIn;
    private String timeOut;
    private int stayDuration24Hours;
    private double totalCost;

    public Reservation(Guest guest, Room room,
                       String checkInDate, String checkOutDate,
                       String timeIn, String timeOut,
                       int stayDuration24Hours) {
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.stayDuration24Hours = stayDuration24Hours;
        this.totalCost = room.getPricePer24Hours() * stayDuration24Hours;
        this.room.setAvailable(false);
    }

    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public String getTimeIn() { return timeIn; }
    public String getTimeOut() { return timeOut; }
    public int getStayDuration24Hours() { return stayDuration24Hours; }
    public double getTotalCost() { return totalCost; }
}

// Main Class: filename must be Main.java
public class Main {
    public static void main(String[] args) {

        // Composition
        // PURPOSE: The hotel creates and owns the 13 rooms
        Hotel hotel = new Hotel();
        List<Room> rooms = hotel.getRooms();

        // In-memory collection for reservations (No database)
        List<Reservation> reservations = new ArrayList<>();

        // Swing GUI
        // Main window (JFrame)
        // PURPOSE: Creates the main application window that holds all the GUI components
        JFrame frame = new JFrame("Room Reservation System");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // Title and rate labels (JLabel)
        // PURPOSE: Displays the system title, room rate, and room capacity at the top of the window
        JLabel titleLabel = new JLabel("ROOM RESERVATION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel rateLabel = new JLabel(
                "Rate: ₱900 / 24 hours | Capacity: Max 2 Adults & 1 Child per room",
                SwingConstants.CENTER
        );

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(titleLabel);
        topPanel.add(rateLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Input fields (JTextField)
        // PURPOSE: Text boxes where the user types the guest and reservation details
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField adultsField = new JTextField();
        JTextField childrenField = new JTextField();
        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();
        JTextField timeInField = new JTextField();
        JTextField timeOutField = new JTextField();
        JTextField durationField = new JTextField();

        // Room dropdown (JComboBox)
        // PURPOSE: Lets the user pick from the list of currently available room numbers
        JComboBox<String> roomComboBox = new JComboBox<>();
        for (Room r : rooms) {
            if (r.isAvailable()) {
                roomComboBox.addItem(r.getRoomNumber());
            }
        }

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        formPanel.add(new JLabel("Guest Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Number of Adults (Max 2):"));
        formPanel.add(adultsField);
        formPanel.add(new JLabel("Number of Children (Max 1):"));
        formPanel.add(childrenField);
        formPanel.add(new JLabel("Room Number:"));
        formPanel.add(roomComboBox);
        formPanel.add(new JLabel("Check-In Date (YYYY-MM-DD):"));
        formPanel.add(checkInField);
        formPanel.add(new JLabel("Check-Out Date (YYYY-MM-DD):"));
        formPanel.add(checkOutField);
        formPanel.add(new JLabel("Time In (HH:MM):"));
        formPanel.add(timeInField);
        formPanel.add(new JLabel("Time Out (HH:MM):"));
        formPanel.add(timeOutField);
        formPanel.add(new JLabel("For how many 24-hour blocks (days) will you stay? "));
        formPanel.add(durationField);

        frame.add(formPanel, BorderLayout.CENTER);

        // Buttons (JButton)
        // PURPOSE: Clickable controls for creating a reservation, viewing all reservations, and exiting
        JButton reserveButton = new JButton("Create Reservation");
        JButton viewButton = new JButton("View All Reservations");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(reserveButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Create reservation button (ActionListener)
        // PURPOSE: Runs when the button is clicked.It reads the form, validates it, and saves the reservation
        reserveButton.addActionListener(e -> {
            String name = nameField.getText();
            String contact = contactField.getText();

            int adults;
            int children;
            try {
                adults = Integer.parseInt(adultsField.getText());
                children = Integer.parseInt(childrenField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid guest number! Reservation failed.");
                return;
            }

            if (adults > 2 || children > 1 || adults < 1 || children < 0) {
                JOptionPane.showMessageDialog(frame,
                        "Error: Each room allows a maximum of 2 Adults and 1 Child! Reservation failed.");
                return;
            }

            boolean hasRooms = false;
            for (Room r : rooms) {
                if (r.isAvailable()) {
                    hasRooms = true;
                    break;
                }
            }

            if (!hasRooms) {
                JOptionPane.showMessageDialog(frame,
                        "Sorry! All rooms are currently occupied.");
                return;
            }

            String roomNum = (String) roomComboBox.getSelectedItem();

            // POLYMORPHISM
            // PURPOSE: A Room reference can hold a StandardRoom object.
            Room selectedRoom = null;

            for (Room r : rooms) {
                if (r.getRoomNumber().equals(roomNum) && r.isAvailable()) {
                    selectedRoom = r;
                    break;
                }
            }

            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid room number or room is already taken.");
                return;
            }

            // POLYMORPHISM
            // PURPOSE: Calls the overridden method on the actual room object.
            String roomType = selectedRoom.getRoomType();

            // Read date and time inputs (JTextField)
            // PURPOSE: Gets the check-in or out dates and times input by the user
            String checkInDate = checkInField.getText();
            String checkOutDate = checkOutField.getText();
            String timeIn = timeInField.getText();
            String timeOut = timeOutField.getText();

            int duration = 0;
            try {
                duration = Integer.parseInt(durationField.getText());
                if (duration <= 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Duration must be greater than zero!");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid duration entered! Reservation cancelled.");
                return;
            }

            Guest guest = new Guest(name, contact, adults, children);
            Reservation newReservation = new Reservation(
                    guest, selectedRoom, checkInDate, checkOutDate,
                    timeIn, timeOut, duration
            );
            reservations.add(newReservation);

            // Successful pop-up (JOptionPane)
            // PURPOSE: Shows the reservation confirmation with the room, room type, and total cost
            JOptionPane.showMessageDialog(frame,
                    "Reservation Successful!\n"
                            + "Room Reserved: Room " + selectedRoom.getRoomNumber() + "\n"
                            + "Room Type: " + roomType + "\n"
                            + "Total Cost to Pay: ₱" + newReservation.getTotalCost());

            roomComboBox.removeItem(roomNum);

            // Reset form (JTextField)
            // PURPOSE: Empties all text boxes so the form is ready for the next guest
            nameField.setText("");
            contactField.setText("");
            adultsField.setText("");
            childrenField.setText("");
            checkInField.setText("");
            checkOutField.setText("");
            timeInField.setText("");
            timeOutField.setText("");
            durationField.setText("");
        });

        viewButton.addActionListener(e -> {
            String reservationDetails = "";

            if (reservations.isEmpty()) {
                reservationDetails = "No reservations have been made yet.";
            } else {
                for (Reservation res : reservations) {
                    reservationDetails +=
                            "Guest: " + res.getGuest().getName() +
                                    " | Room: " + res.getRoom().getRoomNumber() +
                                    " | Room Type: " + res.getRoom().getRoomType() +
                                    " | Guests: " + res.getGuest().getNumberOfAdults() +
                                    " Adult(s), " + res.getGuest().getNumberOfChildren() +
                                    " Child | Duration: " + res.getStayDuration24Hours() +
                                    " day(s) | Total: ₱" + res.getTotalCost() + "\n\n";
                }
            }
            JTextArea textArea = new JTextArea(reservationDetails, 10, 40);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JOptionPane.showMessageDialog(frame, scrollPane,
                    "ALL RESERVATIONS", JOptionPane.PLAIN_MESSAGE);
        });

        // Exit button event (ActionListener)
        // PURPOSE: Closes the main window when the Exit button is clicked
        exitButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}