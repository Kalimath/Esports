package com.example.backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    private static DateTimeFormatter datumF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter tijdF = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatString(LocalDateTime dateTime){
        return formatDate(dateTime) + " " + formatTime(dateTime);
    }

    public static String formatDate(LocalDateTime dateTime){
        return datumF.format(dateTime);
    }
    public static String formatTime(LocalDateTime dateTime){
        return tijdF.format(dateTime);
    }


}
