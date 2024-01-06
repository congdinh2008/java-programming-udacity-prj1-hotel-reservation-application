package com.congdinh.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Calendar;

public class InputHandler {
    public static int getMenuChoice(String menuTitle, ArrayList<String> menuItems, Scanner scanner) {
        displayMenu(menuItems, menuTitle);
        System.out.println("Enter your choice: ");
        int choice = 0;
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice > 0 && choice <= menuItems.size()) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
                scanner.next();
            }
            displayMenu(menuItems, menuTitle);
        }
        return choice;
    }

    public static void displayMenu(ArrayList<String> menuItems, String menuTitle) {
        System.out.printf("=================== %s ===================\n", menuTitle);
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
    }

    // Get date from keyboard
    // Validate date format mm/dd/yyyy
    // Allow user to re-enter date if invalid
    public static Date getDate(Scanner scanner) {
        while (true) {
            String rawDate = scanner.next();
            if (rawDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                String[] dateParts = rawDate.split("/");
                int month = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (month > 0 && month <= 12 && day > 0 && day <= 31 && year > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setLenient(false);
                    calendar.set(year, month - 1, day);
                    try {
                        Date dateObj = calendar.getTime();
                        return dateObj;
                    } catch (Exception e) {
                        System.out.println("Invalid date. Please try again.");
                    }
                } else {
                    System.out.println("Invalid date. Please try again.");
                }
            } else {
                System.out.println("Invalid date. Please try again.");
            }
        }
    }

    public static Date[] getDateRange(Scanner scanner) {
        Date checkInDate;
        Date checkOutDate;
        // Validate check-in date less than check-out date
        while (true) {
            do {
                System.out.println("Enter check-in date (mm/dd/yyyy): ");
                checkInDate = InputHandler.getDate(scanner);
                if (checkInDate == null) {
                    System.out.println("Invalid date format.");
                } else {
                    break;
                }
            } while (true);

            do {
                System.out.println("Enter check-out date (mm/dd/yyyy): ");
                checkOutDate = InputHandler.getDate(scanner);
                if (checkOutDate == null) {
                    System.out.println("Invalid date format.");
                } else {
                    break;
                }
            } while (true);

            if (checkInDate.compareTo(checkOutDate) < 0) {
                break;
            } else {
                System.out.println("Check-in date must be less than check-out date.");
            }
        }
        return new Date[] { checkInDate, checkOutDate };
    }
}
