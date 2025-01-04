package com.booking.booking_system.services;

import com.booking.booking_system.entities.Schedule;
import com.booking.booking_system.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService implements ScheduleServiceInt{
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, List<String> timeSlots) {
        return scheduleRepository.findById(id)
                .map(schedule -> {
                    schedule.setTimeSlots(timeSlots);
                    return scheduleRepository.save(schedule);
                })
                .orElseThrow(()-> new RuntimeException("Schedule not found with ID: " + id));
    }
    public void deleteSchedule(Long id) {
        if(!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found with ID: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> getSchedulesByServiceAndDate(Long serviceId, LocalDate date) {
        return scheduleRepository.findByServiceIdAndDate(serviceId, date);
    }
}
