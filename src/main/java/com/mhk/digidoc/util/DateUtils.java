package com.mhk.digidoc.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    /**
     * Gets the current date and time in the format "DD-MMM-YYYY hh:mm".
     *
     * @return formatted date and time as string
     */
    public static String getCurrentDateTime() {
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the current date and time using the formatter
        return now.format(formatter);
    }

    // Method to calculate age
    public static String getAge(Date dateOfBirth) {
        LocalDate currentDate = LocalDate.now();  // Get today's date
        if (dateOfBirth != null) {


            LocalDate birthDate = dateOfBirth.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();  // Convert Date to LocalDate
            Period period = Period.between(birthDate, currentDate);  // Calculate period between birthdate and current date
            return period.getYears() + " Years";  // Return age in years
        } else {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
    }
}
