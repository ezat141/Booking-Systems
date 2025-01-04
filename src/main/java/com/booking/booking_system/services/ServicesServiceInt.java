package com.booking.booking_system.services;

import com.booking.booking_system.entities.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServicesServiceInt {
    Page<Service> getAllServices(Pageable pageable);
    Service addService(Service service);
    void deleteService(Long id);
    Service updateService(Long id, Service updatedService);
}
