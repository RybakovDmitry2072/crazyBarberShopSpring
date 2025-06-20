package org.example.springcrazybarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "time_slots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean booked;

    @Override
    public String toString() {
        return "TimeSlot{" +
                "id=" + id +
                ", employeeId=" + (employee != null ? employee.getId() : null) +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", booked=" + booked +
                '}';
    }
}
