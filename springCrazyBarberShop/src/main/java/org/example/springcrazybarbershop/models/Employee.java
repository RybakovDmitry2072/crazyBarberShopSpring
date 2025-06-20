package org.example.springcrazybarbershop.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends User {
    private Integer experienceYears;
    private Float salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", experienceYears=" + experienceYears +
                ", salary=" + salary +
                '}';
    }
}
