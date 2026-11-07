import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// INTERFACE
// PURPOSE: Defines the basic operations required by the reservation system.
interface ReservationSystem {
    List<Room> getRooms();
    List<Reservation> getReservations();
    void addReservation(Reservation reservation);
}

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

    // ABSTRACT METHOD
    // PURPOSE: Requires every Room subclass to specify its room type.
    public abstract String getRoomType();
}

// StandardRoom (Inheritance)
class StandardRoom extends Room {
    public StandardRoom(String roomNumber) {
        super(roomNumber, 900.0, 2, 1);
    }

    // POLYMORPHISM (METHOD OVERRIDING)
    @Override
    public String getRoomType() {
        return "Standard Transient Room";
    }
}

// TRANSIENT class change it to implements 
// To manages the transient rooms and reservation.
class Transient implements ReservationSystem {
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public Transient() {
        // Automatically create all 13 Standard Transient Rooms.
        for (int i = 1; i <= 13; i++) {
            rooms.add(new StandardRoom(String.valueOf(i)));
        }
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public List<Reservation> getReservations() {
        return reservations;
    }

    @Override
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
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
    // Aggregation: Reservation uses guest and room objects created separately and these objects can exist independently
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
        // INTERFACE
        // PURPOSE: Main uses the interface type instead of depending directly on the Transient implementation.
        // Creates and owns the 13 rooms
        ReservationSystem transientSystem = new Transient();

        List<Room> rooms = transientSystem.getRooms();
        List<Reservation> reservations = transientSystem.getReservations();

        // Swing GUI
        // Main window (JFrame)
        // PURPOSE: Creates the main application window that holds all the GUI components
        JFrame frame = new JFrame("Transient Room Reservation System");
        frame.setSize(550, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // Title and rate labels (JLabel)
        // PURPOSE: Displays the system title, room rate, and room capacity at the top of the window
        JLabel titleLabel = new JLabel(
                "TRANSIENT ROOM RESERVATION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel rateLabel = new JLabel(
                "Rate: ₱900 / 24 hours | Capacity: Max 2 Adults & 1 Child per room",
                SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        // To add space above and below the title and rate text.
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
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
        for (Room room : rooms) {
            if (room.isAvailable()) {
                roomComboBox.addItem(room.getRoomNumber());
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
        formPanel.add(new JLabel("Stay duration (24-hour blocks):"));
        formPanel.add(durationField);

        frame.add(formPanel, BorderLayout.CENTER);

        // Buttons (JButton)
        // PURPOSE: Clickable controls for creating a reservation, viewing all reservations, and exiting
        JButton reserveButton = new JButton("Create Reservation");
        JButton viewButton = new JButton("View All Reservations");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        // Add space above and below the buttons.
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        buttonPanel.add(reserveButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Create reservation button (ActionListener)
        // PURPOSE: Runs when the button is clicked.It reads the form, validates it, and saves the reservation
        reserveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();

            // Basic required-field validation.
            if (name.isEmpty() || contact.isEmpty()
                    || checkInField.getText().trim().isEmpty()
                    || checkOutField.getText().trim().isEmpty()
                    || timeInField.getText().trim().isEmpty()
                    || timeOutField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please fill in all guest, date, and time fields.");
                return;
            }

            int adults;
            int children;

            // EXCEPTION HANDLING
            // used NumberFormatException occurs if the user types letters or symbols instead of whole numbers in the guest fields.
            try {
                adults = Integer.parseInt(adultsField.getText().trim());
                children = Integer.parseInt(childrenField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid guest number. Enter whole numbers only.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (adults > 2 || children > 1 || adults < 1 || children < 0) {
                JOptionPane.showMessageDialog(frame,
                        "Each room allows 1-2 adults and 0-1 child.");
                return;
            }

            boolean hasRooms = false;
            for (Room room : rooms) {
                if (room.isAvailable()) {
                    hasRooms = true;
                    break;
                }
            }

            if (!hasRooms) {
                JOptionPane.showMessageDialog(frame,
                        "Sorry! All transient rooms are currently occupied.");
                return;
            }

            String roomNum = (String) roomComboBox.getSelectedItem();
            if (roomNum == null) {
                JOptionPane.showMessageDialog(frame,
                        "Please select an available room.");
                return;
            }

            // POLYMORPHISM
            // PURPOSE: A Room reference can hold a StandardRoom object.
            Room selectedRoom = null;
            for (Room room : rooms) {
                if (room.getRoomNumber().equals(roomNum) && room.isAvailable()) {
                    selectedRoom = room;
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
            String checkInDate = checkInField.getText().trim();
            String checkOutDate = checkOutField.getText().trim();
            String timeIn = timeInField.getText().trim();
            String timeOut = timeOutField.getText().trim();

            int duration;
            // EXCEPTION HANDLING
            // To prevent invalid text from crashing the reservation action.
            try {
                duration = Integer.parseInt(durationField.getText().trim());
                if (duration <= 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Duration must be greater than zero.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid duration. Enter a positive whole number.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Guest guest = new Guest(name, contact, adults, children);
            Reservation newReservation = new Reservation(
                    guest, selectedRoom, checkInDate, checkOutDate,
                    timeIn, timeOut, duration);

            // Uses the interface method to save the reservation.
            transientSystem.addReservation(newReservation);

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
            StringBuilder reservationDetails = new StringBuilder();

            if (reservations.isEmpty()) {
                reservationDetails.append("No reservations have been made yet.");
            } else {
                for (Reservation reservation : reservations) {
                    reservationDetails.append(
                            "Guest: ").append(reservation.getGuest().getName())
                            .append(" | Room: ").append(reservation.getRoom().getRoomNumber())
                            .append(" | Room Type: ").append(reservation.getRoom().getRoomType())
                            .append(" | Guests: ").append(reservation.getGuest().getNumberOfAdults())
                            .append(" Adult(s), ").append(reservation.getGuest().getNumberOfChildren())
                            .append(" Child | Duration: ").append(reservation.getStayDuration24Hours())
                            .append(" day(s) | Total: ₱").append(reservation.getTotalCost())
                            .append("\n\n");
                }
            }

            JTextArea textArea = new JTextArea(reservationDetails.toString(), 10, 40);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(frame, scrollPane,
                    "ALL TRANSIENT RESERVATIONS", JOptionPane.PLAIN_MESSAGE);
        });

        // Exit button event (ActionListener)
        // PURPOSE: Closes the main window when the Exit button is clicked
        exitButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}
