package com.booking.booking_system.services;

import com.booking.booking_system.dto.ServiceDTO;
import com.booking.booking_system.entities.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ServicesServiceInt {
    Page<ServiceDTO> getAllServices(Pageable pageable, LocalDate date);
    ServiceDTO addService(ServiceDTO service);
    void deleteService(Long id);
    ServiceDTO updateService(ServiceDTO serviceDTO);
    //    Service updateService(Long id, Service updatedService);
}
