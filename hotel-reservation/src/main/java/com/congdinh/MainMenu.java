package com.congdinh;

import java.util.ArrayList;
import java.util.Scanner;

import com.congdinh.helpers.InputHandler;

public class MainMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
        HotelManager hotelManager = new HotelManager();
        // Load data from file
        hotelManager.loadStateFromFile();

        switch (mainMenuChoice) {
            case 1:
                // Find and reserve a room
                hotelManager.findAndReserveARoomUI(scanner);
                break;
            case 2:
                // See my reservations
                hotelManager.displayMyReservationsUI(scanner);
                break;
            case 3:
                // Create an account
                hotelManager.createACustomerUI(scanner);
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
                main(null);
            } else {
                hotelManager.saveStateToFile();
                System.out.println("Thank you for using our application.");
                scanner.close();
            }
        } else {
            hotelManager.saveStateToFile();
            System.out.println("Thank you for using our application.");
            scanner.close();
        }
    }

    private static void adminManagement(Scanner scanner) {
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
        AdminManager adminManager = new AdminManager();
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
                main(null);
                break;
            default:
                break;
        }

        // check if user wants to continue using the application
        System.out.println("Do you want to continue using the application (Y/N): ");
        String choice = scanner.next();
        if (choice.equalsIgnoreCase("Y")) {
            adminManagement(scanner);
        } else {
            adminManager.saveStateToFile();
        }
    }
}