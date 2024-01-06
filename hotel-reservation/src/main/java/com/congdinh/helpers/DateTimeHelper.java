package com.congdinh.helpers;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeHelper {

    public static Date parseDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date = dateFormat.parse(str);
            return date;
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            return null;
        }
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String dateToString(Date date, SimpleDateFormat dateFormat) {
        if (dateFormat != null) {
            return dateFormat.format(date);
        } else {
            return date.toString();
        }
    }

}
