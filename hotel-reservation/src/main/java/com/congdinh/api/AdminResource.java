package com.congdinh.api;

import java.util.Collection;
import java.util.List;

import com.congdinh.model.Customer;
import com.congdinh.model.IRoom;
import com.congdinh.service.CustomerService;
import com.congdinh.service.ReservationService;

public class AdminResource {
    private static AdminResource adminResource = null;
    private CustomerService customerService = CustomerService.getInstance();
    private ReservationService reservationService = ReservationService.getInstance();

    public static AdminResource getInstance() {
        if (adminResource == null) {
            adminResource = new AdminResource();
        }
        return adminResource;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public IRoom getARoom(String roomNumber) {
        return reservationService.getARoomByRoomNumber(roomNumber);
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }

    public void saveStateToFile() {
        reservationService.saveStateToFile();
    }

    public void loadStateFromFile() {
        reservationService.loadStateFromFile();
    }
}
