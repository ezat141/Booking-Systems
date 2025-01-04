package com.booking.booking_system.controllers;


import com.booking.booking_system.dto.BookingResponse;
import com.booking.booking_system.dto.GeneralResponse;
import com.booking.booking_system.entities.Booking;
import com.booking.booking_system.dto.BookingRequest;
import com.booking.booking_system.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }



    // Get All Bookings
//    @GetMapping
//    public ResponseEntity<List<Booking>> getAllBookings() {
//        List<Booking> bookings = bookingService.getAllBookings();
//        return ResponseEntity.ok(bookings);
//    }

    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookingService.getAllBookings(PageRequest.of(page, size)));
    }


    // Create a New Booking
    @PostMapping
    public ResponseEntity<GeneralResponse<?>> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        try {
            // Delegate booking creation to the service
            BookingResponse createdBooking = bookingService.createBooking(bookingRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(GeneralResponse.success(createdBooking));
        } catch (IllegalArgumentException e) {
            // Handle unavailable time slot
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(GeneralResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            // Handle other errors (e.g., User or Schedule not found)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GeneralResponse.error("An error occurred: " + e.getMessage()));
        }
    }

    @PutMapping("/{bookingId}/confirm")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> confirmBooking(@PathVariable Long bookingId) {
        try {
            BookingResponse confirmedBooking = bookingService.confirmBooking(bookingId);
            return ResponseEntity.ok(confirmedBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




//    @PutMapping("/{id}")
//    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody BookingRequest updatedBooking) {
//        try {
//            Booking updated = bookingService.updateBooking(id, updatedBooking);
//            return ResponseEntity.ok(updated);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

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
    public ResponseEntity<List<BookingResponse>> getUserBookings(@PathVariable Long userId) {
        List<BookingResponse> bookings = bookingService.getUserBookings(userId);
        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(bookings);
    }

}
