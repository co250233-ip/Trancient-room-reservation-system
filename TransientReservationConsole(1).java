import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        // In-memory collections (No database)
        List<Room> rooms = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();

        // Automatically create all 13 Standard Rooms
        for (int i = 1; i <= 13; i++) {
            rooms.add(new StandardRoom(String.valueOf(i)));
        }

        boolean running = true;
        System.out.println("  ROOM RESERVATION  ");

        while (running) {
            System.out.println("\nMENU");
            System.out.println("1. Create a Reservation");
            System.out.println("2. View All Reservations");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            if (choice == 1) {
                System.out.println("\nNEW RESERVATION");
                System.out.println("Rate: ₱900 / 24 hours | Capacity: Max 2 Adults & 1 Child per room");

                System.out.print("Enter Guest Name: ");
                String name = scanner.nextLine();

                System.out.print("Enter Contact Number: ");
                String contact = scanner.nextLine();

                int adults;
                int children;
                try {
                    System.out.print("Enter number of Adults (Max 2): ");
                    adults = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter number of Children (Max 1): ");
                    children = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Invalid guest number! Reservation failed.");
                    continue;
                }

                if (adults > 2 || children > 1 || adults < 1 || children < 0) {
                    System.out.println("Error: Each room allows a maximum of 2 Adults and 1 Child! Reservation failed.");
                    continue;
                }

                boolean hasRooms = false;
                for (Room r : rooms) {
                    if (r.isAvailable()) {
                        hasRooms = true;
                        break;
                    }
                }

                if (!hasRooms) {
                    System.out.println("Sorry! All rooms are currently occupied.");
                    continue;
                }

                System.out.print("Enter Room Number you want to reserve (1-13): ");
                String roomNum = scanner.nextLine();

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
                    System.out.println("Invalid room number or room is already taken.");
                    continue;
                }

                // POLYMORPHISM
                // PURPOSE: Calls the overridden method on the actual room object.
                System.out.println("Selected Room Type: " + selectedRoom.getRoomType());

                System.out.print("Check-In Date (YYYY-MM-DD): ");
                String checkInDate = scanner.nextLine();

                System.out.print("Check-Out Date (YYYY-MM-DD): ");
                String checkOutDate = scanner.nextLine();

                System.out.print("Time In (HH:MM): ");
                String timeIn = scanner.nextLine();

                System.out.print("Time Out (HH:MM): ");
                String timeOut = scanner.nextLine();

                System.out.print("For how many 24-hour blocks (days) will you stay? ");
                int duration = 0;
                try {
                    duration = Integer.parseInt(scanner.nextLine());
                    if (duration <= 0) {
                        System.out.println("Duration must be greater than zero!");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid duration entered! Reservation cancelled.");
                    continue;
                }

                Guest guest = new Guest(name, contact, adults, children);
                Reservation newReservation = new Reservation(
                    guest, selectedRoom, checkInDate, checkOutDate,
                    timeIn, timeOut, duration
                );
                reservations.add(newReservation);

                System.out.println("\nReservation Successful!");
                System.out.println("Room Reserved: Room " + selectedRoom.getRoomNumber());
                System.out.println("Room Type: " + selectedRoom.getRoomType());
                System.out.println("Total Cost to Pay: ₱" + newReservation.getTotalCost());

            } else if (choice == 2) {
                System.out.println("\nALL RESERVATIONS");
                if (reservations.isEmpty()) {
                    System.out.println("No reservations have been made yet.");
                } else {
                    for (Reservation res : reservations) {
                        System.out.println(
                            "Guest: " + res.getGuest().getName() +
                            " | Room: " + res.getRoom().getRoomNumber() +
                            " | Room Type: " + res.getRoom().getRoomType() +
                            " | Guests: " + res.getGuest().getNumberOfAdults() +
                            " Adult(s), " + res.getGuest().getNumberOfChildren() +
                            " Child | Duration: " + res.getStayDuration24Hours() +
                            " day(s) | Total: ₱" + res.getTotalCost()
                        );
                    }
                }

            } else if (choice == 3) {
                running = false;
                System.out.println("Exiting the program. Goodbye!");
            } else {
                System.out.println("Invalid choice. Please choose between 1 and 3.");
            }
        }

        scanner.close();
    }
}
