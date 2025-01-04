package com.booking.booking_system.services;

import com.booking.booking_system.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicesService implements ServicesServiceInt{

    @Autowired
    private ServiceRepository serviceRepository;

    // Fetch all services
//    public List<com.booking.booking_system.entities.Service>getAllServices() {
//        return serviceRepository.findAll();
//    }
    // Fetch all services pages
    public Page<com.booking.booking_system.entities.Service> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    // Add a new service
    public com.booking.booking_system.entities.Service addService(com.booking.booking_system.entities.Service service) {
        return serviceRepository.save(service);
    }

    // Delete a service by ID
    public void deleteService(Long id) {
        Optional<com.booking.booking_system.entities.Service> existingService = serviceRepository.findById(id);
        if (existingService.isEmpty()) {
            throw new IllegalArgumentException("Service with ID " + id + " does not exist.");
        }
        serviceRepository.deleteById(id);
    }

    // Update an existing service
    public com.booking.booking_system.entities.Service updateService(Long id, com.booking.booking_system.entities.Service updatedService) {
        return serviceRepository.findById(id).map(service -> {
            service.setName(updatedService.getName());
            service.setDescription(updatedService.getDescription());
            service.setPrice(updatedService.getPrice());
            service.setDuration(updatedService.getDuration());
            return serviceRepository.save(service);
        }).orElseThrow(() -> new IllegalArgumentException("Service with ID " + id + " not found."));
    }
}
