package org.example.springcrazybarbershop.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // Настраиваем сериализатор для LocalDateTime
        javaTimeModule.addSerializer(java.time.LocalDateTime.class, 
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        
        objectMapper.registerModule(javaTimeModule);
        
        // Отключаем запись дат как временных меток
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Отключаем ошибку при пустых бинах
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        // Игнорируем неизвестные свойства при десериализации
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        // Разрешаем пустые значения для объектов
        objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        
        return objectMapper;
    }
} 