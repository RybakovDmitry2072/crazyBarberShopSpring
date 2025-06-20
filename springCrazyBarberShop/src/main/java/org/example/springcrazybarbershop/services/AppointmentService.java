package org.example.springcrazybarbershop.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.AppointmentDto;
import org.example.springcrazybarbershop.dto.form.AppointmentForm;
import org.example.springcrazybarbershop.exceptions.NotFoundAppointmentException;
import org.example.springcrazybarbershop.mappers.AppointmentFormMapper;
import org.example.springcrazybarbershop.mappers.AppointmentMapper;
import org.example.springcrazybarbershop.models.*;
import org.example.springcrazybarbershop.repositories.AppointmentRepository;
import org.example.springcrazybarbershop.repositories.ClientRepository;
import org.example.springcrazybarbershop.services.interfeces.IAppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService implements IAppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Qualifier("appointmentFormMapper")
    private final AppointmentFormMapper       formMapper;
    @Qualifier("appointmentMapper")
    private final AppointmentMapper           dtoMapper;
    private final ClientRepository            clientRepo;
    private final AppointmentRepository       appointmentRepository;

    @Transactional
    @Override
    public AppointmentDto crateAppointment(AppointmentForm form, String userEmail) {
        try {
            log.debug("Создание новой записи для пользователя с email: {}", userEmail);
            log.debug("Данные формы: {}", form);

            Client client = clientRepo.findUserByEmail(userEmail)
                    .map(Client.class::cast)
                    .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

            Appointment appointment = formMapper.toEntity(form, client);
            TimeSlot timeSlot = appointment.getTimeSlot();
            
            if (timeSlot.isBooked()) {
                throw new IllegalStateException("Выбранное время уже занято");
            }
            
            timeSlot.setBooked(true);
            
            appointment = appointmentRepository.save(appointment);
            log.debug("Запись успешно создана: {}", appointment);
            
            return dtoMapper.toAppointmentDto(appointment);
        } catch (Exception e) {
            logger.error("Ошибка при создании записи: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Page<Appointment> findAllPaginated(int page, int size, String sortDirection) {
        Pageable pageable = PageRequest.of(page, size);
        
        if ("asc".equalsIgnoreCase(sortDirection)) {
            return appointmentRepository.findAllOrderByDateAsc(pageable);
        } else {
            return appointmentRepository.findAllOrderByDateDesc(pageable);
        }
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void deleteById(Long id) {
        try {
            appointmentRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении записи с id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<AppointmentDto> getActiveAppointmentsByClient(Long clientId) {
        List<Appointment> appointments = appointmentRepository.findAllByClient_Id(clientId);
        logger.debug("Найдено заказов пользователя с id {}: {}", clientId,appointments.size());
        return appointments.stream()
                .filter(Objects::nonNull)
                .map(dtoMapper::toAppointmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelReservation(Long id) {
        try {
            appointmentRepository.cancelReservation(id);
        } catch (Exception e){
            logger.error("Невозможно отменить заказ c id: {}. Ошибка: {}", id, e.getMessage());
            throw new NotFoundAppointmentException();
        }
    }
}

