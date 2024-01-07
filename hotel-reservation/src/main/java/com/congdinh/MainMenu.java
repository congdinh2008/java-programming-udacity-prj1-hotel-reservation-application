package com.congdinh;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.congdinh.api.HotelResource;
import com.congdinh.helpers.InputHandler;
import com.congdinh.model.IRoom;
import com.congdinh.model.Reservation;

public class MainMenu {
    private static MainMenu instance = null;
    private static HotelResource hotelResource = HotelResource.getInstance();

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    public void createACustomerUI(Scanner scanner) {
        System.out.println("=================== Create a customer - Hotel Services ===================");
        String newEmail;
        do {
            System.out.println("Enter customer email: ");
            String email = scanner.nextLine();

            // validate email format: example@domain.com
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty.");
                continue;
            } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Invalid email format.");
                continue;
            } else {
                // check if email already exists
                if (hotelResource.getCustomer(email) != null) {
                    System.out.println("Customer with this email already exists.");
                } else {
                    newEmail = email;
                    break;
                }
            }
        } while (true);

        String firstName;
        do {
            System.out.println("Enter customer first name: ");
            firstName = scanner.nextLine();
            if (firstName.isEmpty()) {
                System.out.println("First name cannot be empty.");
            } else {
                break;
            }
        } while (true);

        String lastName;
        do {
            System.out.println("Enter customer last name: ");
            lastName = scanner.nextLine();
            if (lastName.isEmpty()) {
                System.out.println("Last name cannot be empty.");
            } else {
                break;
            }
        } while (true);

        try {
            hotelResource.createACustomer(newEmail, firstName, lastName);
            System.out.println("Customer created successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findAndReserveARoomUI(Scanner scanner) {
        System.out.println("=================== Find and reserve a room - Hotel Services ===================");

        Date[] dates;
        Collection<IRoom> rooms;
        while (true) {
            dates = InputHandler.getDateRange(scanner);
            rooms = hotelResource.findARoom(dates[0], dates[1]);
            if (rooms.size() == 0) {
                System.out.println("No rooms available for this date range.");
                String anotherChoice;
                while (true) {
                    System.out.println("Do you want to choose another date range (Y/N): ");
                    anotherChoice = scanner.next();
                    if (anotherChoice.equalsIgnoreCase("N") || anotherChoice.equalsIgnoreCase("Y")) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
                if (anotherChoice.equalsIgnoreCase("N")) {
                    return;
                } else {
                    continue;
                }
            } else {
                break;
            }
        }
        System.out.println("List of available rooms: ");
        System.out.printf("| %-15s | %-15s | %-15s | %-15s |\n",
                "Room Number", "Price", "Room Type", "Free");
        for (IRoom room : rooms) {
            System.out.println(room);
        }

        String choice;
        while (true) {
            System.out.println("Do you want to book a room (Y/N): ");
            choice = scanner.next();
            if (choice.equalsIgnoreCase("N") || choice.equalsIgnoreCase("Y")) {
                scanner.nextLine();
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        if (choice.equalsIgnoreCase("N")) {
            return;
        } else {
            bookARoomUI(scanner, dates[0], dates[1], rooms);
        }
    }

    private void bookARoomUI(Scanner scanner, Date checkInDate, Date checkOutDate, Collection<IRoom> rooms) {
        System.out.println("=================== Book a room - Hotel Services ===================");
        String email;
        while (true) {
            System.out.println("Enter customer email: ");
            email = scanner.nextLine();
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty.");
            } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Invalid email format.");
            } else {
                break;
            }
        }

        // Create a customer if not exists
        if (hotelResource.getCustomer(email) == null) {
            String firstName;
            do {
                System.out.println("Enter customer first name: ");
                firstName = scanner.nextLine();
                if (firstName.isEmpty()) {
                    System.out.println("First name cannot be empty.");
                } else {
                    break;
                }
            } while (true);

            String lastName;
            do {
                System.out.println("Enter customer last name: ");
                lastName = scanner.nextLine();
                if (lastName.isEmpty()) {
                    System.out.println("Last name cannot be empty.");
                } else {
                    break;
                }
            } while (true);

            try {
                hotelResource.createACustomer(email, firstName, lastName);
                System.out.println("Customer created successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("List of available rooms: ");
        for (IRoom room : rooms) {
            System.out.println(room);
        }

        String roomNumber;
        IRoom room;
        while (true) {
            System.out.println("Enter room number: ");
            roomNumber = scanner.nextLine();
            if (roomNumber.isEmpty()) {
                System.out.println("Room number cannot be empty.");
            }
            room = hotelResource.getRoom(roomNumber);
            if (room == null) {
                System.out.println("Room not found.");
            } else {
                break;
            }
        }

        try {
            Reservation reservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
            System.out.println("Room booked successfully.");
            System.out.println(reservation);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayMyReservationsUI(Scanner scanner) {
        System.out.println("Enter customer email: ");
        String email = scanner.nextLine();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        if (reservations.size() == 0) {
            System.out.println("No reservations found for this customer.");
        } else {
            System.out.println("List of reservations for this customer with email " + email + ": ");
            System.out.printf("| %-20s | %-20s | %-30s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s |\n",
                    "First Name", "Last Name", "Email", "Room Number", "Room Type", "Room Price", "Room Free",
                    "Check In Date", "Check Out Date");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    public void saveStateToFile() {
        hotelResource.saveStateToFile();
    }

    public void loadStateFromFile() {
        hotelResource.loadStateFromFile();
    }

    public void displayMainMenu(Scanner scanner) {
        ArrayList<String> mainMenuItems = new ArrayList<String>() {
            {
                add("Find and reserve a room");
                add("See my reservations");
                add("Create an account");
                add("Admin");
                add("Exit");
            }
        };

        int mainMenuChoice = InputHandler.getMenuChoice("Main Menu", mainMenuItems, scanner);
        scanner.nextLine();
        loadStateFromFile();

        switch (mainMenuChoice) {
            case 1:
                // Find and reserve a room
                findAndReserveARoomUI(scanner);
                break;
            case 2:
                // See my reservations
                displayMyReservationsUI(scanner);
                break;
            case 3:
                // Create an account
                createACustomerUI(scanner);
                break;
            case 4:
                // Admin
                adminManagement(scanner);
                break;
            case 5:
                // Exit the application
                break;
            default:
                break;
        }

        if (mainMenuChoice != 5) {
            // check if user wants to continue using the application
            System.out.println("Do you want to continue using the application (Y/N): ");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("Y")) {
                displayMainMenu(scanner);
            } else {
                saveStateToFile();
                System.out.println("Thank you for using our application.");
                scanner.close();
            }
        } else {
            saveStateToFile();
            System.out.println("Thank you for using our application.");
            scanner.close();
        }
    }

    private void adminManagement(Scanner scanner) {
        ArrayList<String> adminMenuItems = new ArrayList<String>() {
            {
                add("See all Customers");
                add("See all Rooms");
                add("See all Reservations");
                add("Add a Room");
                add("Back to Main Menu");
            }
        };
        int adminMenuChoice = InputHandler.getMenuChoice("Admin Menu", adminMenuItems, scanner);
        AdminMenu adminManager = new AdminMenu();
        // Load data from file
        adminManager.loadStateFromFile();
        switch (adminMenuChoice) {
            case 1:
                // See all Customers
                adminManager.displayAllCustomers();
                break;
            case 2:
                // See all Rooms
                adminManager.displayAllRooms();
                break;
            case 3:
                // See all Reservations
                adminManager.displayAllReservations();
                break;
            case 4:
                // Add a Room
                adminManager.addRoomUI(scanner);
                break;
            case 5:
                // Back to Main Menu
                displayMainMenu(scanner);
                break;
            default:
                break;
        }

        // check if user wants to continue using the application
        System.out.println("Do you want to continue using the admin services (Y/N): ");
        String choice = scanner.next();
        if (choice.equalsIgnoreCase("Y")) {
            adminManagement(scanner);
        } else {
            adminManager.saveStateToFile();
        }
    }
}
