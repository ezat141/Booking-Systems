package com.booking.booking_system.repositories;

import com.booking.booking_system.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByServiceIdAndDate(Long serviceId, LocalDate date);
}
