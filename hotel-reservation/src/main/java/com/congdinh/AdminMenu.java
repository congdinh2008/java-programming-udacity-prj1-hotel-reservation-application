package com.congdinh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.congdinh.api.AdminResource;
import com.congdinh.model.Customer;
import com.congdinh.model.IRoom;
import com.congdinh.model.Room;
import com.congdinh.model.RoomType;

public class AdminMenu {
    private static AdminMenu adminManager = null;

    private AdminResource adminResource = AdminResource.getInstance();

    public static AdminMenu getInstance() {
        if (adminManager == null) {
            adminManager = new AdminMenu();
        }
        return adminManager;
    }

    public void addRoomUI(Scanner scanner) {
        List<IRoom> rooms = new ArrayList<IRoom>();

        do {
            var room = addRoom(scanner, rooms);
            rooms.add(room);
            System.out.println("Do you want to add another room (Y/N): ");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("N")) {
                break;
            } else if (choice.equalsIgnoreCase("Y")) {
                continue;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (true);

        adminResource.addRoom(rooms);
    }

    /*
     * Allow admin to add a room to the system
     * Data input from keyboard
     */
    public Room addRoom(Scanner scanner, List<IRoom> newRooms) {
        System.out.println("=================== Add Room - Admin Manager ===================");

        String roomNumber;
        while (true) {
            System.out.println("Please enter room number: ");
            roomNumber = scanner.next();
            // Check if room number is not a valid number
            if (!roomNumber.matches("[0-9]+")) {
                System.out.println("Invalid room number. Please try again.");
                continue;
            }

            final String finalRoomNumber = roomNumber;
            if (roomNumber != null && !roomNumber.isEmpty()) {
                if (adminResource.getARoom(roomNumber) != null
                        || newRooms.stream().anyMatch(room -> room.getRoomNumber().equalsIgnoreCase(finalRoomNumber))) {
                    System.out.println("Room number already exists. Please try again.");
                    continue;
                }
                break;
            } else {
                System.out.println("Invalid room number. Please try again.");
                scanner.next();
            }
        }

        // Print Enum RoomType to console for admin to choose

        int roomType = 0;
        while (true) {
            int i = 1;
            for (RoomType type : RoomType.values()) {
                System.out.println(i + ". " + type);
                i++;
            }

            System.out.println("Please enter room type: ");

            if (scanner.hasNextInt()) {
                roomType = scanner.nextInt();
                if (roomType > 0 && roomType <= RoomType.values().length) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
                scanner.next();
            }
        }

        // Allow admin to set if room is free

        int free = 0;
        while (true) {
            System.out.println("Please enter if room is free (1/2): ");
            System.out.println("1. True");
            System.out.println("2. False");
            if (scanner.hasNextInt()) {
                free = scanner.nextInt();
                if (free > 0 && free <= 2) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
                scanner.next();
            }
        }

        if (free == 1) {
            return new Room(roomNumber, 0.0, RoomType.values()[roomType - 1], true);
        }

        // Allow admin to set price for room
        double price = 0.0;
        while (true) {
            System.out.println("Please enter price per night (price > 0): ");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                if (price > 0) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
                scanner.next();
            }
        }

        return new Room(roomNumber, price, RoomType.values()[roomType - 1], free == 1 ? true : false);
    }

    /*
     * Allow admin to see all rooms in the system
     */
    public void displayAllRooms() {
        System.out.println("=================== All Rooms - Admin Manager ===================");
        Collection<IRoom> rooms = adminResource.getAllRooms();
        System.out.printf("| %-15s | %-15s | %-15s | %-15s |\n",
                "Room Number", "Price", "Room Type", "Free");
        for (IRoom room : rooms) {
            System.out.println(room);
        }
    }

    /*
     * Allow admin to see all reservations in the system
     */
    public void displayAllReservations() {
        System.out.println("=================== All Reservations - Admin Manager ===================");
        adminResource.displayAllReservations();
    }

    /*
     * Allow admin to see all customers in the system
     */
    public void displayAllCustomers() {
        System.out.println("=================== All Customers - Admin Manager ===================");
        Collection<Customer> customers = adminResource.getAllCustomers();
        System.out.printf("| %-20s | %-20s | %-30s |\n",
                "First Name", "Last Name", "Email");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    public void saveStateToFile() {
        adminResource.saveStateToFile();
    }

    public void loadStateFromFile() {
        adminResource.loadStateFromFile();
    }
}
