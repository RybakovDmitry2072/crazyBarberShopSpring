package org.example.springcrazybarbershop.converters;

import org.example.springcrazybarbershop.utils.DateTimeFormatConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalDateTimeConverter implements Converter<LocalDateTime, String> {

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern(DateTimeFormatConstants.DATE_TIME_FORMAT)
            .withLocale(new Locale("ru"));

    @Override
    public String convert(LocalDateTime source) {
        if (source == null) {
            return "";
        }
        return formatter.format(source);
    }
} 