package org.example.springcrazybarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @ManyToOne(optional = false)
    @JoinColumn(name = "haircut_category_id")
    private HaircutCategory haircutCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;
}

