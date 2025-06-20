package org.example.springcrazybarbershop.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.springcrazybarbershop.converters.StringToLocalDateTimeConverter;
import org.example.springcrazybarbershop.dto.TimeSlotDto;
import org.example.springcrazybarbershop.mappers.TimeSlotMapper;
import org.example.springcrazybarbershop.models.TimeSlot;
import org.example.springcrazybarbershop.repositories.TimeSlotRepository;
import org.example.springcrazybarbershop.services.interfeces.ITimeSlotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotService implements ITimeSlotService {
    private static final Logger logger = LoggerFactory.getLogger(TimeSlotService.class);

    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;
    private final StringToLocalDateTimeConverter dateTimeConverter;

    @Override
    public List<TimeSlotDto> getTimeSlotsByEmployeeId(Long employeeId) {
        logger.debug("Получение свободных временных слотов для сотрудника с ID: {}", employeeId);
        List<TimeSlot> timeSlots = timeSlotRepository.findTimeSlotByEmployee_IdAndBookedFalse(employeeId);
        logger.debug("Найдено {} свободных временных слотов", timeSlots.size());
        return timeSlots.stream()
                .map(timeSlotMapper::toTimeSlotDto)
                .peek(dto -> logger.debug("Преобразован слот: {}", dto))
                .toList();
    }

    @Override
    public TimeSlotDto updateTimeSlotStatus(Long timeSlotId, boolean booked) {
        logger.debug("Обновление статуса временного слота с ID: {}, booked: {}", timeSlotId, booked);
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new EntityNotFoundException("TimeSlot not found with id: " + timeSlotId));
        timeSlot.setBooked(booked);
        TimeSlot updatedTimeSlot = timeSlotRepository.save(timeSlot);
        logger.debug("Статус временного слота обновлен: {}", updatedTimeSlot);
        return timeSlotMapper.toTimeSlotDto(updatedTimeSlot);
    }

    @Override
    public List<TimeSlotDto> getFilteredTimeSlots(Long employeeId, String startAfter, String endBefore) {
        logger.debug("Фильтрация временных слотов для сотрудника с ID: {}", employeeId);
        logger.debug("Параметры фильтрации: startAfter={}, endBefore={}", startAfter, endBefore);
        
        LocalDateTime startDateTime = startAfter != null ? dateTimeConverter.convert(startAfter) : null;
        LocalDateTime endDateTime = endBefore != null ? dateTimeConverter.convert(endBefore) : null;
        
        logger.debug("Преобразованные даты: startDateTime={}, endDateTime={}", startDateTime, endDateTime);
        
        List<TimeSlot> slots = timeSlotRepository.findByFilters(employeeId, startDateTime, endDateTime);
        logger.debug("Найдено {} слотов", slots.size());
        int i = 0;
        return slots.stream()
                .sorted(Comparator.comparing(TimeSlot::getStartTime))
                .map(timeSlotMapper::toTimeSlotDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeSlot> findAll() {
        logger.debug("Получение всех временных слотов");
        return timeSlotRepository.findAll();
    }

    @Override
    @Transactional
    public TimeSlot save(TimeSlot timeSlot) {
        logger.debug("Сохранение временного слота: {}", timeSlot);
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Удаление временного слота с ID: {}", id);
        timeSlotRepository.deleteById(id);
    }

    @Override
    public Optional<TimeSlot> findById(Long id) {
        logger.debug("Поиск временного слота по ID: {}", id);
        return timeSlotRepository.findById(id);
    }
}
