package org.example.springcrazybarbershop.repositories;

import org.example.springcrazybarbershop.models.Employee;
import org.example.springcrazybarbershop.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e JOIN e.userRoles ur WHERE ur.role IN :roles")
    Page<Employee> findAllByRole(Pageable pageable, Set<Role> roles);

    Optional<Employee> findById(Long id);
}
