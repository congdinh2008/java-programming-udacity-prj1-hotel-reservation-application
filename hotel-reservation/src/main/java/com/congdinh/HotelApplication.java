package com.congdinh;

import java.util.Scanner;

public class HotelApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MainMenu hotelManager = new MainMenu();

        hotelManager.displayMainMenu(scanner);
    }
}