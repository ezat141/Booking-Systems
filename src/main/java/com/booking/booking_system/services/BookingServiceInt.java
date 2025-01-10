package com.booking.booking_system.services;

import com.booking.booking_system.dto.BookingRequest;
import com.booking.booking_system.dto.BookingResponse;
import com.booking.booking_system.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingServiceInt {
    Page<BookingResponse> getAllBookings(Pageable pageable);
    BookingResponse getBookingById(Long bookingId);
    BookingResponse createBooking(BookingRequest bookingRequest);
    boolean cancelBooking(Long bookingId);
    List<BookingResponse> getUserBookings(Long userId);
    BookingResponse confirmBooking(Long bookingId);

}
