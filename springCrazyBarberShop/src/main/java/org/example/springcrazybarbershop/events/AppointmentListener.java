package org.example.springcrazybarbershop.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.exceptions.SmsException;
import org.example.springcrazybarbershop.services.interfeces.ISmsService;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointmentListener implements ApplicationListener<onAppointmentCompleteEvent> {

    private final ISmsService smsService;

    @Override
    @Async
    public void onApplicationEvent(onAppointmentCompleteEvent event) {
        try {
            this.confirmAppointment(event);
            log.info("SMS уведомление успешно отправлено на номер: {}", event.getPhone());
        } catch (SmsException e) {
            log.error("Не удалось отправить SMS уведомление на номер: {}. Ошибка: {}", 
                event.getPhone(), e.getMessage());
        } catch (Exception e) {
            log.error("Неожиданная ошибка при отправке SMS: {}", e.getMessage());
        }
    }

    private void confirmAppointment(onAppointmentCompleteEvent event) {
        smsService.sendSms(event.getPhone(), event.getText());
    }
}
