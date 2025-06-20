package org.example.springcrazybarbershop.controllers.api;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.dto.EmployeeDto;
import org.example.springcrazybarbershop.models.Role;
import org.example.springcrazybarbershop.services.interfeces.IEmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final IEmployeeService employeeService;

    @GetMapping
    public Page<EmployeeDto> getAllEmployees(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "role", defaultValue = "BARBER") Role role) {

        return employeeService.getAllEmployeeByRole(page, size, role);
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable("id") Long id){

        return employeeService.getEmployee(id);
    }

}
