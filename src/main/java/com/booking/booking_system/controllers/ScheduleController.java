package com.booking.booking_system.controllers;

import com.booking.booking_system.dto.ScheduleDTO;
import com.booking.booking_system.dto.ScheduleRequest;
import com.booking.booking_system.dto.TimeSlotRequest;
import com.booking.booking_system.entities.Schedule;
import com.booking.booking_system.entities.Service;
import com.booking.booking_system.repositories.ServiceRepository;
import com.booking.booking_system.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;


    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;

    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PostMapping
//    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule schedule) {
//        Schedule createdSchedule = scheduleService.createSchedule(schedule);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
//    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        ScheduleDTO scheduleDTO = scheduleService.createSchedule(scheduleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleDTO);
    }

    // Get Schedules by Service ID and Date
    @GetMapping("/{serviceId}/{date}")
    public ResponseEntity<List<Schedule>> getSchedule(@PathVariable Long serviceId, @PathVariable String date) {
        List <Schedule> schedules = scheduleService.getSchedulesByServiceAndDate(serviceId, LocalDate.parse(date));
        return ResponseEntity.ok(schedules);
    }

    // Update Schedule Time Slots
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @RequestBody TimeSlotRequest timeSlotRequest) {
        try {
            List<String> timeSlots = timeSlotRequest.getTimeSlots();
            Schedule updatedSchedule = scheduleService.updateSchedule(id, timeSlots);
            return ResponseEntity.ok(updatedSchedule);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request format for timeSlots.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
