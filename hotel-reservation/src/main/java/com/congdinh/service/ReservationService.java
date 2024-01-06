package com.congdinh.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.congdinh.helpers.DateTimeHelper;
import com.congdinh.model.Customer;
import com.congdinh.model.IRoom;
import com.congdinh.model.Reservation;
import com.congdinh.model.Room;
import com.congdinh.model.RoomType;

public class ReservationService {
    private final static String HOTEL_APPLICATION_DATA = "Hotel_Application_Data.xlsx";
    private final static String ROOMS_SHEET = "Rooms";
    private final static String RESERVATIONS_SHEET = "Reservations";

    private static ReservationService reservationService = null;
    private Collection<Reservation> reservations;
    private Collection<IRoom> rooms;

    public ReservationService() {
        this.reservations = new ArrayList<Reservation>();
        this.rooms = new ArrayList<IRoom>();
    }

    public static ReservationService getInstance() {
        if (reservationService == null) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }

    public void addRoom(IRoom room) {
        this.rooms.add(room);
    }

    public IRoom getARoom(String roomNumber) {
        for (IRoom room : this.rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        this.reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> rooms = new ArrayList<IRoom>();
        // Find all rooms that are not reserved
        // Room is not reserved if checkInDate and checkOutDate are not between the
        // room's checkInDate and checkOutDate
        for (IRoom room : this.rooms) {
            boolean isReserved = false;
            for (Reservation reservation : this.reservations) {
                if (reservation.getRoom().getRoomNumber().equalsIgnoreCase(room.getRoomNumber())) {
                    if (checkInDate.after(reservation.getCheckInDate())
                            && checkInDate.before(reservation.getCheckOutDate())) {
                        isReserved = true;
                        break;
                    } else if (checkOutDate.after(reservation.getCheckInDate())
                            && checkOutDate.before(reservation.getCheckOutDate())) {
                        isReserved = true;
                        break;
                    }
                }
            }
            if (!isReserved) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    public Collection<IRoom> getAllRooms() {
        return this.rooms;
    }

    public IRoom getARoomByRoomNumber(String roomNumber) {
        return this.rooms.stream().filter(room -> room.getRoomNumber().equalsIgnoreCase(roomNumber)).findFirst()
                .orElse(null);
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> reservations = new ArrayList<Reservation>();
        for (Reservation reservation : this.reservations) {
            if (reservation.getCustomer().getEmail().equalsIgnoreCase(customer.getEmail())) {
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public void printAllReservation() {
        System.out.printf("| %-20s | %-20s | %-30s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s |\n",
                "First Name", "Last Name", "Email", "Room Number", "Room Type", "Room Price", "Room Free",
                "Check In Date", "Check Out Date");

        for (Reservation reservation : this.reservations) {
            System.out.println(reservation);
        }
    }

    public void readRoomsFromFile() {
        // Read all rooms from file
        // If file not exist, do nothing
        // If file exist, read all rooms from file
        try {
            File file = new File(HOTEL_APPLICATION_DATA);
            // Check if file not exist, create a new file
            if (!file.exists()) {
                System.out.println("File not found.");
                return;
            } else {
                // If file exists, open it
                try (FileInputStream fileIn = new FileInputStream(HOTEL_APPLICATION_DATA)) {
                    Workbook workbook = new XSSFWorkbook(fileIn);

                    if (workbook.getSheet(ROOMS_SHEET) == null) {
                        workbook.close();
                        System.out.println("Sheet not found.");
                        return;
                    }

                    Sheet sheet = workbook.getSheet(ROOMS_SHEET);

                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        String roomNumber = row.getCell(0).getStringCellValue();
                        double roomPrice = row.getCell(1).getNumericCellValue();
                        RoomType roomType = row.getCell(2).getStringCellValue().equals("Single") ? RoomType.SINGLE
                                : RoomType.DOUBLE;
                        boolean roomFree = row.getCell(3).getStringCellValue().equals("Yes") ? true : false;

                        if (this.getARoomByRoomNumber(roomNumber) == null) {
                            this.rooms.add(new Room(roomNumber, roomPrice, roomType, roomFree));
                        }
                    }

                    workbook.close();
                } catch (IOException e) {
                    System.out.println("Error while opening file.");
                    return; // If there's an error opening the file, exit the method
                }
            }
        } catch (Exception e) {
            System.out.println("Error while reading rooms from file.");
        }
    }

    public void readReservationsFromFile() {
        // Read all reservations from file
        // If file not exist, do nothing
        // If file exist, read all reservations from file
        try {
            File file = new File(HOTEL_APPLICATION_DATA);

            // Check if file not exist, create a new file
            if (!file.exists()) {
                System.out.println("File not found.");
                return;
            } else {
                // If file exists, open it
                FileInputStream fileIn = new FileInputStream(HOTEL_APPLICATION_DATA);
                Workbook workbook = new XSSFWorkbook(fileIn);

                if (workbook.getSheet(RESERVATIONS_SHEET) == null) {
                    workbook.close();
                    System.out.println("Sheet not found.");
                    return;
                }

                Sheet sheet = workbook.getSheet(RESERVATIONS_SHEET);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    String firstName = row.getCell(0).getStringCellValue();
                    String lastName = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();
                    String roomNumber = row.getCell(3).getStringCellValue();
                    double roomPrice = row.getCell(4).getNumericCellValue();
                    RoomType roomType = row.getCell(5).getStringCellValue().equals("Single") ? RoomType.SINGLE
                            : RoomType.DOUBLE;
                    boolean roomFree = row.getCell(6).getStringCellValue().equals("Yes") ? true : false;
                    Date checkInDate = DateTimeHelper.parseDate(row.getCell(7).getStringCellValue());
                    Date checkOutDate = DateTimeHelper.parseDate(row.getCell(8).getStringCellValue());

                    Customer customer = new Customer(firstName, lastName, email);
                    IRoom room = new Room(roomNumber, roomPrice, roomType, roomFree);

                    // check if reservation already exists
                    boolean reservationExists = false;
                    for (Reservation reservation : this.reservations) {
                        if (reservation.getCustomer().getEmail().equals(customer.getEmail())
                                && reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())
                                && reservation.getCheckInDate().equals(checkInDate)
                                && reservation.getCheckOutDate().equals(checkOutDate)) {
                            reservationExists = true;
                            break;
                        }
                    }

                    if (!reservationExists) {
                        this.reservations.add(new Reservation(customer, room, checkInDate, checkOutDate));
                    }
                }

                fileIn.close();
                workbook.close();
            }

        } catch (Exception e) {
            System.out.println("Error while reading reservations from file: " + e.getMessage());
        }
    }

    // Write all collection data to file
    public void saveStateToFile() {
        // Write all reservations to file
        // Create a new file if not exist
        // If exist, overwrite it
        writeReservationsToFile();
        // Write all rooms to file
        // Create a new file if not exist
        // If exist, overwrite it
        writeRoomsToFile();
    }

    private void writeRoomsToFile() {
        if (this.rooms == null || this.rooms.isEmpty()) {
            return;
        } else {
            // Write all rooms to file
            // Create a new file if not exist
            // If exist, overwrite it
            try {
                File file = new File(HOTEL_APPLICATION_DATA);
                Workbook workbook;
                // Check if file not exist, create a new file
                if (!file.exists()) {
                    workbook = new XSSFWorkbook();
                } else {
                    // If file exists, open it
                    try (FileInputStream fileIn = new FileInputStream(HOTEL_APPLICATION_DATA)) {
                        workbook = new XSSFWorkbook(fileIn);
                    } catch (IOException e) {
                        System.out.println("Error while opening file: " + e.getMessage());
                        return; // If there's an error opening the file, exit the method
                    }
                }

                if (workbook.getSheet(ROOMS_SHEET) != null) {
                    workbook.removeSheetAt(workbook.getSheetIndex(ROOMS_SHEET));
                }

                Sheet sheet = workbook.createSheet(ROOMS_SHEET);

                // Create header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Room Number");
                headerRow.createCell(1).setCellValue("Room Price");
                headerRow.createCell(2).setCellValue("Room Type");
                headerRow.createCell(3).setCellValue("Room Free");

                int rowNum = 1;
                for (IRoom room : this.rooms) {
                    Row row = sheet.createRow(rowNum++);

                    row.createCell(0).setCellValue(room.getRoomNumber());
                    row.createCell(1).setCellValue(room.getRoomPrice());
                    row.createCell(2)
                            .setCellValue(room.getRoomType() == RoomType.SINGLE ? "Single" : "Double");
                    row.createCell(3).setCellValue(room.isFree() ? "Yes" : "No");
                }

                try (FileOutputStream fileOut = new FileOutputStream(HOTEL_APPLICATION_DATA)) {
                    workbook.write(fileOut);
                } catch (Exception e) {
                    System.out.println("Error writing rooms to file: " + e.getMessage());
                }

                workbook.close();
            } catch (Exception e) {
                System.out.println("Error writing rooms to file: " + e.getMessage());
            }
        }
    }

    private void writeReservationsToFile() {
        if (this.reservations == null || this.reservations.isEmpty()) {
            return;
        }
        // Write collection of reservation to file
        // Create a new file if not exist
        // If exist, overwrite it

        try {
            File file = new File(HOTEL_APPLICATION_DATA);
            Workbook workbook;
            // Check if file not exist, create a new file
            if (!file.exists()) {
                workbook = new XSSFWorkbook();
            } else {
                // If file exists, open it
                try (FileInputStream fileIn = new FileInputStream(HOTEL_APPLICATION_DATA)) {
                    workbook = new XSSFWorkbook(fileIn);
                } catch (IOException e) {
                    System.out.println("Error while opening file: " + e.getMessage());
                    return; // If there's an error opening the file, exit the method
                }
            }

            if (workbook.getSheet(RESERVATIONS_SHEET) != null) {
                workbook.removeSheetAt(workbook.getSheetIndex(RESERVATIONS_SHEET));
            }

            Sheet sheet = workbook.createSheet(RESERVATIONS_SHEET);

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Customer First Name");
            headerRow.createCell(1).setCellValue("Customer Last Name");
            headerRow.createCell(2).setCellValue("Customer Email");
            headerRow.createCell(3).setCellValue("Room Number");
            headerRow.createCell(4).setCellValue("Room Price");
            headerRow.createCell(5).setCellValue("Room Type");
            headerRow.createCell(6).setCellValue("Room Free");
            headerRow.createCell(7).setCellValue("Check In Date");
            headerRow.createCell(8).setCellValue("Check Out Date");

            int rowNum = 1;
            for (Reservation reservation : this.reservations) {
                Row row = sheet.createRow(rowNum++);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                row.createCell(0).setCellValue(reservation.getCustomer().getFirstName());
                row.createCell(1).setCellValue(reservation.getCustomer().getLastName());
                row.createCell(2).setCellValue(reservation.getCustomer().getEmail());
                row.createCell(3).setCellValue(reservation.getRoom().getRoomNumber());
                row.createCell(4).setCellValue(reservation.getRoom().getRoomPrice());
                row.createCell(5)
                        .setCellValue(reservation.getRoom().getRoomType() == RoomType.SINGLE ? "Single" : "Double");
                row.createCell(6).setCellValue(reservation.getRoom().isFree() ? "Yes" : "No");
                row.createCell(7).setCellValue(DateTimeHelper.dateToString(reservation.getCheckInDate(), dateFormat));
                row.createCell(8).setCellValue(DateTimeHelper.dateToString(reservation.getCheckOutDate(), dateFormat));
            }

            try (FileOutputStream fileOut = new FileOutputStream(HOTEL_APPLICATION_DATA)) {
                workbook.write(fileOut);
            } catch (Exception e) {
                System.out.println("Error writing reservations to file: " + e.getMessage());
            }

            workbook.close();
        } catch (Exception e) {
            System.out.println("Error writing reservations to file: " + e.getMessage());
        }
    }

    public void loadStateFromFile() {
        readRoomsFromFile();
        readReservationsFromFile();
    }
}
