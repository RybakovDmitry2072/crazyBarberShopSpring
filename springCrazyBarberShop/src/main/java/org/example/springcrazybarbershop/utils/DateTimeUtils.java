package org.example.springcrazybarbershop.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtils {
    
    private static final Locale RU_LOCALE = new Locale("ru");
    
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern(pattern)
                .withLocale(RU_LOCALE)
                .format(dateTime);
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DateTimeFormatConstants.DATE_TIME_FORMAT);
    }
    
    public static String formatDateTimeWithFullMonth(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DateTimeFormatConstants.DATE_TIME_FULL_MONTH_FORMAT);
    }
    
    public static String formatDate(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DateTimeFormatConstants.DATE_FORMAT);
    }
    
    public static String formatTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DateTimeFormatConstants.TIME_FORMAT);
    }
} 