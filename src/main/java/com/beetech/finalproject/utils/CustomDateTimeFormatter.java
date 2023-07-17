package com.beetech.finalproject.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CustomDateTimeFormatter {
    public static LocalDate dateOfBirthFormatter(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date;
    }

    public static LocalDate dateOfOrder() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(date);
        LocalDate localDate = LocalDate.parse(currentDate);
        return localDate;
    }
}
