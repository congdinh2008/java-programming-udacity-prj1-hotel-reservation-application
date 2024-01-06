package com.congdinh.model;

import java.util.Date;

import com.congdinh.helpers.DateTimeHelper;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public IRoom getRoom() {
        return this.room;
    }

    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public Date getCheckOutDate() {
        return this.checkOutDate;
    }

    @Override
    public String toString() {
        return String.format("| %-20s | %-20s | %-30s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s |",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.isFree() ? "Yes" : "No",
                DateTimeHelper.dateToString(checkInDate),
                DateTimeHelper.dateToString(checkOutDate));
    }
}
