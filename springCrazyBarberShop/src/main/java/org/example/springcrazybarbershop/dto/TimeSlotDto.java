package org.example.springcrazybarbershop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDto {

    private Long id;

    private Long employeeId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean booked;
}
