package org.example.springcrazybarbershop.configs;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class LoggingConfig {

    @PostConstruct
    public void init() {
        // Основные сервисы
        setupLogger("org.example.springcrazybarbershop.services.AppointmentService", "logs/appointment.log");
        setupLogger("org.example.springcrazybarbershop.services.EmployeeService", "logs/employee.log");
        setupLogger("org.example.springcrazybarbershop.services.ClientService", "logs/client.log");

        // Аутентификация и безопасность
        setupLogger("org.example.springcrazybarbershop.services.AuthService", "logs/auth.log");
        setupLogger("org.example.springcrazybarbershop.services.PasswordResetService", "logs/password.log");

        // Коммуникации
        setupLogger("org.example.springcrazybarbershop.services.SmsService", "logs/sms.log");
        setupLogger("org.example.springcrazybarbershop.services.NotificationService", "logs/notification.log");

        // Файлы и изображения
        setupLogger("org.example.springcrazybarbershop.services.ImageService", "logs/image.log");

        // Расписание и категории
        setupLogger("org.example.springcrazybarbershop.services.TimeSlotService", "logs/timeslot.log");
        setupLogger("org.example.springcrazybarbershop.services.HaircutCategoryService", "logs/haircut.log");
    }

    private void setupLogger(String loggerName, String logFile) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        
        Logger logger = loggerContext.getLogger(loggerName);
        logger.setAdditive(false);
        
        RollingFileAppender appender = new RollingFileAppender();
        appender.setContext(loggerContext);
        appender.setName(loggerName + "Appender");
        appender.setFile(logFile);
        
        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setParent(appender);
        rollingPolicy.setFileNamePattern(logFile + ".%d{yyyy-MM-dd}.log");
        rollingPolicy.setMaxHistory(30); // Храним логи за 30 дней
        rollingPolicy.start();
        
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();
        
        appender.setRollingPolicy(rollingPolicy);
        appender.setEncoder(encoder);
        appender.start();
        
        logger.addAppender(appender);
    }
} 