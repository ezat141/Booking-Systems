package com.booking.booking_system.controllers;


import com.booking.booking_system.entities.Booking;
import com.booking.booking_system.entities.BookingStatus;
import com.booking.booking_system.entities.Service;
import com.booking.booking_system.entities.User;
import com.booking.booking_system.model.BookingRequest;
import com.booking.booking_system.repositories.BookingRepository;
import com.booking.booking_system.repositories.ServiceRepository;
import com.booking.booking_system.repositories.UserRepository;
import com.booking.booking_system.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired

    private UserRepository userRepository;
    @Autowired

    private ServiceRepository serviceRepository;
    @Autowired

    private BookingRepository bookingRepository;


    // Get All Bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

//    // Create a New Booking
//    @PostMapping
//    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
//        Booking createdBooking = bookingService.createBooking(booking);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
//    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        try {
            // Retrieve User and Service entities by ID
            User user = userRepository.findById(bookingRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Service service = serviceRepository.findById(bookingRequest.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            // Create a new Booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setService(service);
            booking.setTimeSlot(bookingRequest.getTimeSlot());
            booking.setStatus(bookingRequest.getStatus());

            // Save the booking
            Booking createdBooking = bookingRepository.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody BookingRequest updatedBooking) {
        try {
            Booking updated = bookingService.updateBooking(id, updatedBooking);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        boolean isCancelled = bookingService.cancelBooking(id);
        if (isCancelled) {
            return ResponseEntity.ok("Booking cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found or already cancelled.");
        }
    }

    // Get User Bookings
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bookings found for user ID " + userId);
        }
        return ResponseEntity.ok(bookings);
    }

}
