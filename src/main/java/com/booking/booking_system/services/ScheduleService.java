package com.booking.booking_system.services;

import com.booking.booking_system.dto.ScheduleDTO;
import com.booking.booking_system.dto.ScheduleRequest;
import com.booking.booking_system.dto.ServiceDTO;
import com.booking.booking_system.entities.Schedule;
import com.booking.booking_system.repositories.ScheduleRepository;
import com.booking.booking_system.repositories.ServiceRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements ScheduleServiceInt{
    private final ScheduleRepository scheduleRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, ServiceRepository serviceRepository, ModelMapper modelMapper) {
        this.scheduleRepository = scheduleRepository;
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional // Ensure this method runs in a transaction
    public ScheduleDTO createSchedule(ScheduleRequest scheduleRequest) {
        // Fetch the Service entity
        com.booking.booking_system.entities.Service service = serviceRepository.findById(scheduleRequest.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + scheduleRequest.getServiceId()));

        // Debug log to confirm the Service entity is fetched
        System.out.println("Fetched Service: " + service);

        // Map ScheduleRequest to Schedule entity
        Schedule schedule = new Schedule();
        schedule.setDate(scheduleRequest.getDate());
        schedule.setTimeSlots(scheduleRequest.getTimeSlots());
        schedule.setService(service); // Set the Service entity

        // Save the Schedule entity
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // Debug log to confirm the Schedule entity is saved
        System.out.println("Saved Schedule: " + savedSchedule);

        // Map the saved Schedule entity to ScheduleDTO
        ScheduleDTO scheduleDTO = modelMapper.map(savedSchedule, ScheduleDTO.class);

        // Map the Service entity to ServiceDTO
        scheduleDTO.setService(modelMapper.map(service, ServiceDTO.class));

        return scheduleDTO;
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
    public List<String> getAvailableTimeSlots(Long serviceId, LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByServiceIdAndDate(serviceId, date);
        return schedules.stream()
                .flatMap(schedule -> schedule.getTimeSlots().stream())
                .collect(Collectors.toList());
    }
    public Optional<Schedule> findByServiceIdAndTimeSlot(Long serviceId, String timeSlot) {
        return scheduleRepository.findByServiceIdAndTimeSlot(serviceId, timeSlot);
    }

}
