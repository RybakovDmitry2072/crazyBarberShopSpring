package org.example.springcrazybarbershop.exceptions;

public class NotFoundAppointmentException extends NotFoundException{
    public NotFoundAppointmentException() {
        super("No found appointment by id");
    }
}
