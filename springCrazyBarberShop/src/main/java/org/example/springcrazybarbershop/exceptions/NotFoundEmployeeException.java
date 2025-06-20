package org.example.springcrazybarbershop.exceptions;

public class NotFoundEmployeeException extends NotFoundException{
    public NotFoundEmployeeException() {
        super("No found user");
    }
}
