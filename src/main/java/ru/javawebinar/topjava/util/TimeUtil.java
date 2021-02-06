package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DateTimeFormatter formatDateTo = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter formatDateFrom = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static String formatDateTime(LocalDateTime time) {
        return formatDateTo.format(time);
    }

    public static LocalDateTime getDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, formatDateFrom);
    }

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }
}
