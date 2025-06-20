package org.example.springcrazybarbershop.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@ControllerAdvice
@Slf4j
public class HtmlExceptionHandler {

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(Exception ex, Model model, HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            throw new RuntimeException("API 404, перенаправьте в JSON-обработчик");
        }

        log.warn("Страница не найдена: {}", request.getRequestURI());
        model.addAttribute("message", "Страница не найдена");
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        log.warn("Доступ запрещен: {}", ex.getMessage());
        model.addAttribute("message", "У вас нет доступа к этой странице");
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalServerError(Exception ex, Model model) {
        log.error("500 Internal Server Error", ex);
        model.addAttribute("message", "Внутренняя ошибка сервера");
        return "error/500";
    }
}