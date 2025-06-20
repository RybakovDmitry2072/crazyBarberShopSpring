package org.example.springcrazybarbershop.repositories;

import org.example.springcrazybarbershop.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findTimeSlotByEmployee_Id(Long employeeId);
    
    List<TimeSlot> findTimeSlotByEmployee_IdAndBookedFalse(Long employeeId);

    @Query("""
        SELECT t FROM TimeSlot t 
        WHERE t.employee.id = :employeeId 
        AND t.booked = false 
        AND (COALESCE(:startAfter, t.startTime) <= t.startTime) 
        AND (COALESCE(:endBefore, t.endTime) >= t.endTime)
    """)
    List<TimeSlot> findByFilters(@Param("employeeId") Long employeeId,
                                @Param("startAfter") LocalDateTime startAfter,
                                @Param("endBefore") LocalDateTime endBefore);

}
