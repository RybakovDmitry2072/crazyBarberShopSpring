package org.example.springcrazybarbershop.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class EmployeeDto extends UserDto {

    private Integer experienceYears;

    private Float salary;
}
