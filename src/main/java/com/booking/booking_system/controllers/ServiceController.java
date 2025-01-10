package com.booking.booking_system.controllers;

import com.booking.booking_system.dto.ServiceDTO;
import com.booking.booking_system.entities.Service;
import com.booking.booking_system.repositories.ServiceRepository;
import com.booking.booking_system.services.ScheduleService;
import com.booking.booking_system.services.ServicesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServicesService servicesService;
    @Autowired
    private ScheduleService scheduleService;

    public ServiceController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

//    @GetMapping
//    public ResponseEntity<Page<ServiceDTO>> getAllServices(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Page<ServiceDTO> services = servicesService.getAllServices(PageRequest.of(page, size));
//        return ResponseEntity.ok(services);
//    }
    @GetMapping
    public ResponseEntity<Page<ServiceDTO>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDate date) {
        Page<ServiceDTO> services = servicesService.getAllServices(PageRequest.of(page, size), date);
        return ResponseEntity.ok(services);
    }

    // Add a new service (Admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceDTO> addService(@Valid @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO savedServiceDTO = servicesService.addService(serviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedServiceDTO);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping()
    public ResponseEntity<ServiceDTO> updateService(@Valid @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO savedServiceDTO = servicesService.updateService(serviceDTO);
        return ResponseEntity.ok(savedServiceDTO);
    }


    // Delete a service
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        servicesService.deleteService(id);
        return ResponseEntity.ok("Service deleted successfully!");
    }



    // Get available time slots for a service (accessible to any authenticated user)
    @GetMapping("/{serviceId}/schedules")
    public ResponseEntity<List<String>> getAvailableTimeSlots(
            @PathVariable Long serviceId,
            @RequestParam String date) {
        List<String> timeSlots = scheduleService.getAvailableTimeSlots(serviceId, LocalDate.parse(date));
        return ResponseEntity.ok(timeSlots);
    }


}
