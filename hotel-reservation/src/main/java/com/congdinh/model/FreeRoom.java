package com.congdinh.model;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, Double roomPrice, RoomType roomType, boolean free) {
        super(roomNumber, roomPrice, roomType, free);
        this.setRoomPrice(0.0);
    }

    @Override
    public String toString() {
        return "FreeRoom{" +
                "roomNumber='" + getRoomNumber() + '\'' +
                ", roomPrice=" + getRoomPrice() +
                ", roomType=" + getRoomType() +
                ", free=" + isFree() +
                '}';
    }
}
