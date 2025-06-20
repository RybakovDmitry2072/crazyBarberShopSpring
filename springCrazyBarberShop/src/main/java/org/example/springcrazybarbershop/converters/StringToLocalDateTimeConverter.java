package org.example.springcrazybarbershop.converters;

import org.example.springcrazybarbershop.utils.DateTimeFormatConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter
            .ofPattern(DateTimeFormatConstants.DATE_TIME_FORMAT)
            .withLocale(new Locale("ru"));

    private static final DateTimeFormatter SIMPLE_FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm")
            .withLocale(new Locale("ru"));

    @Override
    public LocalDateTime convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }

        String trimmedSource = source.trim();
        try {
            return LocalDateTime.parse(trimmedSource, FULL_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(trimmedSource, SIMPLE_FORMATTER);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Could not parse date time: " + trimmedSource);
            }
        }
    }
} 