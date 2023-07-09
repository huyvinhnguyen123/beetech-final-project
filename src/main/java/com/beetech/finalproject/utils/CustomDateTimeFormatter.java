package com.beetech.finalproject.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {
    public static LocalDate dateOfBirthFormatter(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date;
    }
}
