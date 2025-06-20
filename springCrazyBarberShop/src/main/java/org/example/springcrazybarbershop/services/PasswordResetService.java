package org.example.springcrazybarbershop.services;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.models.PasswordResetToken;
import org.example.springcrazybarbershop.repositories.PasswordTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@RequiredArgsConstructor
@Service
public class PasswordResetService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private final PasswordTokenRepository passwordTokenRepository;

    public String validatePasswordResetToken(String token) {
        try {
            logger.debug("Проверка токена сброса пароля: {}", token);
            final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

            if (!isTokenFound(passToken)) {
                logger.error("Токен сброса пароля не найден: {}", token);
                return "invalidToken";
            }
            if (isTokenExpired(passToken)) {
                logger.error("Токен сброса пароля истек: {}", token);
                return "expired";
            }
            logger.debug("Токен сброса пароля валиден: {}", token);
            return null;
        } catch (Exception e) {
            logger.error("Ошибка при валидации токена сброса пароля: {}", e.getMessage(), e);
            throw e;
        }
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    @Transactional
    public void deletePasswordResetToken(String token) {
        try {
            logger.debug("Попытка удаления токена сброса пароля: {}", token);
            PasswordResetToken passwordToken = passwordTokenRepository.findByToken(token);
            if (passwordToken != null) {
                passwordTokenRepository.delete(passwordToken);
                logger.debug("Токен сброса пароля успешно удален: {}", token);
            } else {
                logger.warn("Попытка удаления несуществующего токена: {}", token);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении токена сброса пароля: {}", e.getMessage(), e);
            throw e;
        }
    }
}
