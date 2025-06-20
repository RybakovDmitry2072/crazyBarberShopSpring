package org.example.springcrazybarbershop.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentForm {

    private Long haircutCategoryId;

    private Long employeeId;

    private Long timeSlotId;

}
