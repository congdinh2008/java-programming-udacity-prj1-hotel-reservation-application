package com.congdinh.api;

import java.util.Collection;
import java.util.Date;

import com.congdinh.model.Customer;
import com.congdinh.model.IRoom;
import com.congdinh.model.Reservation;
import com.congdinh.service.CustomerService;
import com.congdinh.service.ReservationService;

public class HotelResource {
    private static HotelResource hotelResource = null;
    private CustomerService customerService = CustomerService.getInstance();
    private ReservationService reservationService = ReservationService.getInstance();

    public static HotelResource getInstance() {
        if (hotelResource == null) {
            hotelResource = new HotelResource();
        }
        return hotelResource;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date CheckOutDate) {
        Customer customer = customerService.getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, CheckOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = customerService.getCustomer(customerEmail);
        return reservationService.getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }

    public void saveStateToFile() {
        customerService.saveStateToFile();
        reservationService.saveStateToFile();
    }

    public void loadStateFromFile() {
        customerService.loadStateFromFile();
        reservationService.loadStateFromFile();
    }
}
