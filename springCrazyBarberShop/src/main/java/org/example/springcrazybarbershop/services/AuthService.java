package org.example.springcrazybarbershop.services;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.exceptions.NotFountUserExceptions;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.models.User;
import org.example.springcrazybarbershop.repositories.ClientRepository;
import org.example.springcrazybarbershop.repositories.UserRepository;
import org.example.springcrazybarbershop.security.details.UserDetailsImpl;
import org.example.springcrazybarbershop.services.interfeces.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final ClientRepository clientRepository;

    @Override
    public Client getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.error("Попытка получить текущего пользователя без аутентификации");
                throw new AccessDeniedException("User not authenticated");
            }

            if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
                logger.error("Неверный тип Principal: {}", authentication.getPrincipal().getClass());
                throw new IllegalStateException("Principal type mismatch");
            }

            Long id = userDetails.getUser().getId();
            logger.debug("Получение текущего пользователя с ID: {}", id);

            return clientRepository.findClientById(id)
                    .orElseThrow(() -> {
                        logger.error("Пользователь с ID {} не найден в базе данных", id);
                        return new NotFountUserExceptions();
                    });
        } catch (Exception e) {
            logger.error("Ошибка при получении текущего пользователя: {}", e.getMessage(), e);
            throw e;
        }
    }
}
