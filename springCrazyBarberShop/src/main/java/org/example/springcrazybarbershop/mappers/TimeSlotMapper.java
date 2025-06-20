package org.example.springcrazybarbershop.mappers;

import org.example.springcrazybarbershop.dto.TimeSlotDto;
import org.example.springcrazybarbershop.models.TimeSlot;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    TimeSlotDto toTimeSlotDto(TimeSlot timeSlot);

    @Mapping(target = "employee", ignore = true)
    TimeSlot toTimeSlot(TimeSlotDto timeSlotDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromDto(TimeSlotDto dto, @MappingTarget TimeSlot timeSlot);
}
