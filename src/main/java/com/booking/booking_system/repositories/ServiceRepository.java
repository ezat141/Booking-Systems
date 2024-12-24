package com.booking.booking_system.repositories;

import com.booking.booking_system.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {

}
