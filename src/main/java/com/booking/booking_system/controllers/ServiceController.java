package com.booking.booking_system.controllers;

import com.booking.booking_system.entities.Service;
import com.booking.booking_system.repositories.ServiceRepository;
import com.booking.booking_system.services.ScheduleService;
import com.booking.booking_system.services.ServicesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    // List all services
//    @GetMapping
//    public ResponseEntity<List<Service>> getAllServices() {
//        List<Service> services = servicesService.getAllServices();
//        return ResponseEntity.ok(services);
//    }

    @GetMapping
    public ResponseEntity<Page<Service>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Service> services = servicesService.getAllServices(PageRequest.of(page, size));
        return ResponseEntity.ok(services);
    }

    // Add a new service (Admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Service> addService(@Valid @RequestBody Service service) {
        Service newService = servicesService.addService(service);
        return ResponseEntity.status(201).body(newService);
    }


    // Delete a service
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        servicesService.deleteService(id);
        return ResponseEntity.ok("Service deleted successfully!");
    }

    @GetMapping("/{serviceId}/schedules")
    public ResponseEntity<List<String>> getAvailableTimeSlots(
            @PathVariable Long serviceId,
            @RequestParam String date) {
        List<String> timeSlots = scheduleService.getAvailableTimeSlots(serviceId, LocalDate.parse(date));
        return ResponseEntity.ok(timeSlots);
    }


}
