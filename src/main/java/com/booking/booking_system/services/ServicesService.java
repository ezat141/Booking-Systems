package com.booking.booking_system.services;

import com.booking.booking_system.dto.ServiceDTO;
import com.booking.booking_system.entities.Schedule;
import com.booking.booking_system.exceptions.CustomException;
import com.booking.booking_system.repositories.ScheduleRepository;
import com.booking.booking_system.repositories.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicesService implements ServicesServiceInt{

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Fetch all services
//    public List<com.booking.booking_system.entities.Service>getAllServices() {
//        return serviceRepository.findAll();
//    }
    // Fetch all services pages
    public Page<ServiceDTO> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(service -> {
                    // Fetch all schedules for the service
                    List<Schedule> schedules = scheduleRepository.findByServiceId(service.getId());

                    // Aggregate timeSlots from all schedules
                    List<String> timeSlots = schedules.stream()
                            .flatMap(schedule -> schedule.getTimeSlots().stream())
                            .collect(Collectors.toList());

                    // Map Service entity to ServiceDTO using ModelMapper
                     ServiceDTO serviceDTO = modelMapper.map(service, ServiceDTO.class);

                    // Set the timeSlots in the DTO
                    serviceDTO.setTimeSlots(timeSlots);

                    return serviceDTO;
                });

    }

//    public Page<ServiceDTO> getAllServices(Pageable pageable, LocalDate date) {
//        return serviceRepository.findAll(pageable)
//                .map(service -> {
//                    // Fetch all schedules for the service
//                    List<Schedule> schedules = scheduleRepository.findByServiceIdAndDate(service.getId(), date);
//
//                    // Aggregate timeSlots from all schedules
//                    List<String> timeSlots = schedules.stream()
//                            .flatMap(schedule -> schedule.getTimeSlots().stream())
//                            .collect(Collectors.toList());
//
//                    // Map Service entity to ServiceDTO using ModelMapper
//                    ServiceDTO serviceDTO = modelMapper.map(service, ServiceDTO.class);
//
//                    // Set the timeSlots in the DTO
//                    serviceDTO.setTimeSlots(timeSlots);
//
//                    return serviceDTO;
//                });
//
//    }



    // Add a new service
//    public com.booking.booking_system.entities.Service addService(com.booking.booking_system.entities.Service service) {
//        return serviceRepository.save(service);
//    }

    public ServiceDTO addService(ServiceDTO serviceDTO) {

        if(serviceDTO.getName() == null || serviceDTO.getName().isEmpty()) {
            throw new CustomException("Service name is required.");
        }
        if (serviceDTO.getDescription() == null || serviceDTO.getDescription().isEmpty()) {
            throw new CustomException("ServiceDTO description is required.");
        }
        if (serviceDTO.getPrice() == null) {
            throw new CustomException("ServiceDTO price is required.");
        }
        if (serviceDTO.getDuration() == null) {
            throw new CustomException("ServiceDTO duration is required.");
        }
        com.booking.booking_system.entities.Service service = serviceRepository.save(modelMapper.map(serviceDTO, com.booking.booking_system.entities.Service.class));
        return modelMapper.map(service, ServiceDTO.class);
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
//    public com.booking.booking_system.entities.Service updateService(Long id, com.booking.booking_system.entities.Service updatedService) {
//        return serviceRepository.findById(id).map(service -> {
//            service.setName(updatedService.getName());
//            service.setDescription(updatedService.getDescription());
//            service.setPrice(updatedService.getPrice());
//            service.setDuration(updatedService.getDuration());
//            return serviceRepository.save(service);
//        }).orElseThrow(() -> new IllegalArgumentException("Service with ID " + id + " not found."));
//    }

    public ServiceDTO updateService(ServiceDTO serviceDTO) {
        com.booking.booking_system.entities.Service serviceEntity = serviceRepository.findById(serviceDTO.getId())
                .orElseThrow(() -> new CustomException("Service with ID " + serviceDTO.getId() + " does not exist."));
        if(serviceDTO.getName() != null) {
            serviceEntity.setName(serviceDTO.getName());
        }
        if(serviceDTO.getDescription() != null) {
            serviceEntity.setDescription(serviceDTO.getDescription());
        }
        if(serviceDTO.getPrice() != null) {
            serviceEntity.setPrice(serviceDTO.getPrice());
        }
        if(serviceDTO.getDuration() != null) {
            serviceEntity.setDuration(serviceDTO.getDuration());
        }
        com.booking.booking_system.entities.Service SavedServiceEntity = serviceRepository.save(serviceEntity);
        return modelMapper.map(SavedServiceEntity, ServiceDTO.class);

    }
}
