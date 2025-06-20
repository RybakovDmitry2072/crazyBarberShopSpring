package org.example.springcrazybarbershop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.dto.EmployeeDto;
import org.example.springcrazybarbershop.exceptions.NotFoundEmployeeException;
import org.example.springcrazybarbershop.mappers.EmployeeMapper;
import org.example.springcrazybarbershop.models.Employee;
import org.example.springcrazybarbershop.models.Role;
import org.example.springcrazybarbershop.repositories.EmployeeRepository;
import org.example.springcrazybarbershop.services.interfeces.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeService implements IEmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Page<EmployeeDto> getAllEmployeeByRole(int page, int size, Role role) {
        try {
            logger.debug("Получение списка сотрудников по роли: {}, страница: {}, размер: {}", role, page, size);
            Pageable pageable = PageRequest.of(page, size, Sort.by("firstName"));
            Set<Role> roles = Collections.singleton(role);
            Page<Employee> employees = employeeRepository.findAllByRole(pageable, roles);
            logger.debug("Найдено {} сотрудников с ролью {}", employees.getTotalElements(), role);
            return employees.map(employeeMapper::toEmployeeDto);
        } catch (Exception e) {
            logger.error("Ошибка при получении списка сотрудников по роли {}: {}", role, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public EmployeeDto getEmployee(Long id) throws NotFoundEmployeeException {
        try {
            logger.debug("Получение сотрудника по ID: {}", id);
            EmployeeDto employee = employeeMapper.toEmployeeDto(employeeRepository.findById(id)
                    .orElseThrow(NotFoundEmployeeException::new));
            logger.debug("Найден сотрудник: {}", employee);
            return employee;
        } catch (NotFoundEmployeeException e) {
            logger.warn("Сотрудник с ID {} не найден", id);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при получении сотрудника с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Employee> findAll() {
        try {
            logger.debug("Получение списка всех сотрудников");
            List<Employee> employees = employeeRepository.findAll();
            logger.debug("Найдено {} сотрудников", employees.size());
            return employees;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка всех сотрудников: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        try {
            logger.debug("Сохранение сотрудника: {}", employee);
            Employee savedEmployee = employeeRepository.save(employee);
            logger.info("Сотрудник успешно сохранен с ID: {}", savedEmployee.getId());
            return savedEmployee;
        } catch (Exception e) {
            logger.error("Ошибка при сохранении сотрудника: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            logger.debug("Удаление сотрудника с ID: {}", id);
            employeeRepository.deleteById(id);
            logger.info("Сотрудник с ID {} успешно удален", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении сотрудника с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        try {
            logger.debug("Поиск сотрудника по ID: {}", id);
            Optional<Employee> employee = employeeRepository.findById(id);
            if (employee.isPresent()) {
                logger.debug("Найден сотрудник с ID: {}", id);
            } else {
                logger.debug("Сотрудник с ID {} не найден", id);
            }
            return employee;
        } catch (Exception e) {
            logger.error("Ошибка при поиске сотрудника с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

}