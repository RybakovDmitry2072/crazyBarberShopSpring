package org.example.springcrazybarbershop.mappers;

import org.example.springcrazybarbershop.dto.EmployeeDto;
import org.example.springcrazybarbershop.models.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {org.example.springcrazybarbershop.mappers.FileMapper.class})
public interface EmployeeMapper {

    EmployeeDto toEmployeeDto(Employee employee);

    Employee toEmployee(EmployeeDto dto);

    void updateEmployeeFromDto(EmployeeDto dto, @MappingTarget Employee entity);
}
