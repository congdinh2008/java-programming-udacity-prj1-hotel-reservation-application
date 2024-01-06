package com.congdinh.model;

public class Room implements IRoom {
    private String roomNumber;
    private Double roomPrice;
    private RoomType roomType;
    private boolean free;

    public Room(String roomNumber, Double roomPrice, RoomType roomType, boolean free) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
        this.free = free;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Double roomPrice) {
        if (roomPrice > 0) {
            this.roomPrice = roomPrice;
        }
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public boolean isFree() {
        return free;
    }

    @Override
    public String toString() {
        return String.format("| %-15s | %-15.2f | %-15s | %-15s |",
                roomNumber,
                roomPrice,
                roomType,
                free ? "Yes" : "No");
    }
}
