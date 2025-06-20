package org.example.springcrazybarbershop.exceptions;

public class SmsException extends RuntimeException{
    public SmsException(String message) {
        super(message);
    }
}
