package org.example.springcrazybarbershop.mappers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.form.AppointmentForm;
import org.example.springcrazybarbershop.models.Appointment;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.models.HaircutCategory;
import org.example.springcrazybarbershop.repositories.EmployeeRepository;
import org.example.springcrazybarbershop.repositories.HaircutCategoryRepository;
import org.example.springcrazybarbershop.repositories.TimeSlotRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
@Slf4j
public abstract class AppointmentFormMapper {

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Autowired
    protected TimeSlotRepository timeSlotRepository;

    @Autowired
    protected HaircutCategoryRepository haircutCategoryRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentStatus", constant = "BOOKED")
    @Mapping(target = "client", source = "client")
    @Mapping(target = "employee", expression = "java(getEmployee(form.getEmployeeId()))")
    @Mapping(target = "timeSlot", expression = "java(getTimeSlot(form.getTimeSlotId()))")
    @Mapping(target = "haircutCategory", expression = "java(getHaircutCategory(form.getHaircutCategoryId()))")
    public abstract Appointment toEntity(AppointmentForm form, Client client);

    protected org.example.springcrazybarbershop.models.Employee getEmployee(Long employeeId) {
        log.info("Получение сотрудника с ID: {}", employeeId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    log.error("Сотрудник с ID {} не найден", employeeId);
                    return new EntityNotFoundException("Сотрудник не найден");
                });
    }

    protected org.example.springcrazybarbershop.models.TimeSlot getTimeSlot(Long timeSlotId) {
        log.info("Получение временного слота с ID: {}", timeSlotId);
        return timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> {
                    log.error("Временной слот с ID {} не найден", timeSlotId);
                    return new EntityNotFoundException("Временной слот не найден");
                });
    }

    protected HaircutCategory getHaircutCategory(Long haircutCategoryId) {
        log.info("Получение категории стрижки с ID: {}", haircutCategoryId);
        return haircutCategoryRepository.findById(haircutCategoryId)
                .orElseThrow(() -> {
                    log.error("Категория стрижки с ID {} не найдена", haircutCategoryId);
                    return new EntityNotFoundException("Категория стрижки не найдена");
                });
    }
}
