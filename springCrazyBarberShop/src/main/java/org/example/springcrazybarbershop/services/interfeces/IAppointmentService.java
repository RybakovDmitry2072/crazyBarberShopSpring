package org.example.springcrazybarbershop.services.interfeces;

import org.example.springcrazybarbershop.dto.AppointmentDto;
import org.example.springcrazybarbershop.dto.form.AppointmentForm;

import java.util.List;

public interface IAppointmentService {

    AppointmentDto crateAppointment(AppointmentForm form, String userEmail);

    List<AppointmentDto> getActiveAppointmentsByClient(Long clientId);

    void cancelReservation(Long id);

}
