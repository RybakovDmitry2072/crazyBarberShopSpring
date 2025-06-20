package org.example.springcrazybarbershop.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class onAppointmentCompleteEvent extends ApplicationEvent {
    private String text;
    private String phone;

    public onAppointmentCompleteEvent(Object source, String text, String phone) {
        super(source);
        this.text = text;
        this.phone = phone;
    }
}
