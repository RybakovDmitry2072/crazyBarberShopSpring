package org.example.springcrazybarbershop.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final ClientRepository clientRepository;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.sender-name}")
    private String senderName;

    @Async("taskExecutor")
    public void sendBulkNotification(String subject, String message) {
        logger.info("Начинаем массовую рассылку уведомлений");
        try {
            int totalClients = 0;
            int successCount = 0;
            int errorCount = 0;

            var clients = clientRepository.findAll();
            totalClients = clients.size();
            logger.info("Найдено {} клиентов для рассылки", totalClients);

            for (var client : clients) {
                if (client.getEmail() != null && !client.getEmail().isEmpty()) {
                    try {
                        logger.info("Попытка отправки email для клиента: {} ({})", client.getEmail(), client.getId());
                        sendEmail(client.getEmail(), subject, message);
                        successCount++;
                        logger.info("Уведомление успешно отправлено клиенту: {} ({}/{})", 
                            client.getEmail(), successCount, totalClients);
                    } catch (Exception e) {
                        errorCount++;
                        logger.error("Ошибка при отправке уведомления клиенту {} ({}): {}", 
                            client.getEmail(), client.getId(), e.getMessage(), e);
                    }
                } else {
                    logger.warn("Пропущен клиент без email: ID={}", client.getId());
                }
            }

            logger.info("Массовая рассылка уведомлений завершена. Успешно: {}, Ошибок: {}, Всего: {}", 
                successCount, errorCount, totalClients);
        } catch (Exception e) {
            logger.error("Критическая ошибка при выполнении рассылки: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void sendEmail(String to, String subject, String message) throws MessagingException {
        logger.debug("Подготовка к отправке email для {}", to);
        logger.debug("Параметры отправки: from={}, senderName={}", fromEmail, senderName);
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setFrom(fromEmail, senderName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
            
            logger.debug("Подготовлено сообщение для {}: subject='{}', messageLength={}", 
                to, subject, message.length());
            
            mailSender.send(mimeMessage);
            logger.info("Email успешно отправлен: {}", to);
            
        } catch (MailException e) {
            logger.error("Ошибка SMTP при отправке email для {}: {}", to, e.getMessage(), e);
            throw new MessagingException("Ошибка SMTP: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отправке email для {}: {}", to, e.getMessage(), e);
            throw new MessagingException("Неожиданная ошибка: " + e.getMessage(), e);
        }
    }
} 