package com.booking.booking_system.controllers;

import com.booking.booking_system.entities.Service;
import com.booking.booking_system.repositories.ServiceRepository;
import com.booking.booking_system.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    @Autowired
    private ServicesService servicesService;

    // List all services
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = servicesService.getAllServices();
        return ResponseEntity.ok(services);
    }

    // Add a new service (Admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Service> addService(@RequestBody Service service) {
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


}
