package org.example.springcrazybarbershop.services.interfeces;

import org.example.springcrazybarbershop.exceptions.SmsException;

public interface ISmsService {
    void sendSms(String phone, String text) throws SmsException;
}
