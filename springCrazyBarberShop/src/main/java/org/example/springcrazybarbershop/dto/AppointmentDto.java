package org.example.springcrazybarbershop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springcrazybarbershop.models.HaircutCategory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private Long id;

    private TimeSlotDto timeSlotDto;

    private EmployeeDto employeeDto;

    private HaircutCategory haircutCategory;

    private String status;

}
