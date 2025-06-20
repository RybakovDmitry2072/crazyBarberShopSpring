package org.example.springcrazybarbershop.services.interfeces;

import org.example.springcrazybarbershop.dto.TimeSlotDto;
import org.example.springcrazybarbershop.models.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITimeSlotService {

    List<TimeSlotDto> getTimeSlotsByEmployeeId(Long employeeId);

    TimeSlotDto updateTimeSlotStatus(Long timeSlotId, boolean booked);

    List<TimeSlotDto> getFilteredTimeSlots(Long employeeId, String startAfter, String endBefore);

    // CRUD operations
    List<TimeSlot> findAll();
    TimeSlot save(TimeSlot timeSlot);
    void deleteById(Long id);
    Optional<TimeSlot> findById(Long id);
}
