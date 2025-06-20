package org.example.springcrazybarbershop.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.AppointmentDto;
import org.example.springcrazybarbershop.dto.form.AppointmentForm;
import org.example.springcrazybarbershop.events.onAppointmentCompleteEvent;
import org.example.springcrazybarbershop.exceptions.NotFoundAppointmentException;
import org.example.springcrazybarbershop.models.User;
import org.example.springcrazybarbershop.services.interfeces.IAppointmentService;
import org.example.springcrazybarbershop.services.interfeces.IAuthService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final IAppointmentService appointmentService;
    private final ApplicationEventPublisher eventPublisher;
    private final IAuthService authService;

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentForm appointmentForm) {
        try {
            log.debug("Получен запрос на создание записи: {}", appointmentForm);
            
            User user = authService.getCurrentUser();
            log.debug("Текущий пользователь: {}", user);
            
            AppointmentDto createdAppointment = appointmentService.crateAppointment(appointmentForm, user.getEmail());
            log.debug("Запись успешно создана: {}", createdAppointment);
            
            try {
                eventPublisher.publishEvent(new onAppointmentCompleteEvent(this, "Ваша запись создана", user.getPhone()));
            } catch (Exception e) {
                log.error("Ошибка при отправке уведомления: {}", e.getMessage());
            }
            
            return ResponseEntity.ok(createdAppointment);
        } catch (Exception e) {
            log.error("Ошибка при создании записи: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllActiveAppointmentByUserId(@RequestParam Long clientId) {
        return ResponseEntity.ok(
                appointmentService.getActiveAppointmentsByClient(clientId)
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try {
            appointmentService.cancelReservation(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundAppointmentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
