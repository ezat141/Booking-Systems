package com.booking.booking_system.repositories;

import com.booking.booking_system.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.service.id = :serviceId AND :timeSlot MEMBER OF s.timeSlots")
    Optional<Schedule> findByServiceIdAndTimeSlot(@Param("serviceId") Long serviceId, @Param("timeSlot") String timeSlot);

    List<Schedule> findByServiceIdAndDate(Long serviceId, LocalDate date);

    List<Schedule> findByServiceId(Long serviceId);
}
