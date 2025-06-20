package org.example.springcrazybarbershop.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.TimeSlotDto;
import org.example.springcrazybarbershop.services.interfeces.ITimeSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/timeslots")
@RestController
public class TimeSlotController {

    private final ITimeSlotService timeSlotService;

    @GetMapping
    public ResponseEntity<List<TimeSlotDto>> getFreeTimeSlots(@RequestParam("employeeId") Long employeeId,
                                                          @RequestParam(value = "startAfter", required = false) String startAfter,
                                                          @RequestParam(value = "endBefore", required = false) String endBefore){
        log.debug("Получен запрос на поиск временных слотов:");
        log.debug("employeeId: {}", employeeId);
        log.debug("startAfter: {}", startAfter);
        log.debug("endBefore: {}", endBefore);
        
        return ResponseEntity.ok(timeSlotService.getFilteredTimeSlots(employeeId, startAfter, endBefore));
    }
}
