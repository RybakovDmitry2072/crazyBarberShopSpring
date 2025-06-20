package org.example.springcrazybarbershop.services.interfeces;

import org.example.springcrazybarbershop.dto.EmployeeDto;
import org.example.springcrazybarbershop.exceptions.NotFoundEmployeeException;
import org.example.springcrazybarbershop.models.Employee;
import org.example.springcrazybarbershop.models.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {

    Page<EmployeeDto> getAllEmployeeByRole(int page, int size, Role role);

    EmployeeDto getEmployee(Long id) throws NotFoundEmployeeException;

    // CRUD operations
    List<Employee> findAll();
    Employee save(Employee employee);
    void deleteById(Long id);
    Optional<Employee> findById(Long id);

}
