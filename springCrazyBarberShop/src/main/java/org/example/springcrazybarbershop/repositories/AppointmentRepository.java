package org.example.springcrazybarbershop.repositories;

import jakarta.transaction.Transactional;
import org.example.springcrazybarbershop.models.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Appointment a WHERE a.client.id = :clientId")
    void deleteAllByClientId(Long clientId);
    
    Page<Appointment> findAll(Pageable pageable);
    
    @Query("SELECT a FROM Appointment a ORDER BY a.timeSlot.startTime DESC")
    Page<Appointment> findAllOrderByDateDesc(Pageable pageable);
    
    @Query("SELECT a FROM Appointment a ORDER BY a.timeSlot.startTime ASC")
    Page<Appointment> findAllOrderByDateAsc(Pageable pageable);

    @Query(
            "select a from Appointment a " +
                    "where a.appointmentStatus in (org.example.springcrazybarbershop.models.AppointmentStatus.BOOKED) " +
                    "and a.client.id = :clientId " +
                    "and a.timeSlot.startTime >= CURRENT_TIMESTAMP " +
                    "order by a.timeSlot.startTime asc"
    )
    List<Appointment> findAllByClient_Id(Long clientId);



    @Modifying
    @Transactional
    @Query("""
    UPDATE Appointment a 
    SET a.appointmentStatus = org.example.springcrazybarbershop.models.AppointmentStatus.CANCELLED 
    WHERE a.id = :id
    """)
    void cancelReservation(@Param("id") Long id);
}
