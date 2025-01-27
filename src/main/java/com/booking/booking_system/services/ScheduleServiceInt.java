package com.booking.booking_system.services;

import com.booking.booking_system.dto.ScheduleDTO;
import com.booking.booking_system.dto.ScheduleRequest;
import com.booking.booking_system.entities.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleServiceInt {
    ScheduleDTO createSchedule(ScheduleRequest scheduleRequest);
    Schedule updateSchedule(Long id, List<String> timeSlots);
    void deleteSchedule(Long id);
    List<Schedule> getSchedulesByServiceAndDate(Long serviceId, LocalDate date);
}
