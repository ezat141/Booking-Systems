package com.booking.booking_system.services;

import com.booking.booking_system.entities.Booking;
import com.booking.booking_system.entities.BookingStatus;
import com.booking.booking_system.entities.Schedule;
import com.booking.booking_system.entities.User;
import com.booking.booking_system.exceptions.TimeSlotUnavailableException;
import com.booking.booking_system.model.BookingRequest;
import com.booking.booking_system.repositories.BookingRepository;
import com.booking.booking_system.repositories.ScheduleRepository;
import com.booking.booking_system.repositories.ServiceRepository;
import com.booking.booking_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;



    // Get All Bookings
//    public List<Booking> getAllBookings() {
//        return bookingRepository.findAll();
//    }

    // Get All Bookings pages
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }



    // Create a New Booking
    public Booking createBooking(BookingRequest bookingRequest) {
        // Retrieve User and Schedule
        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Schedule schedule = scheduleRepository.findById(bookingRequest.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // Validate time slot availability
        if (!schedule.getTimeSlots().contains(bookingRequest.getTimeSlot())) {
            throw new IllegalArgumentException("The time slot " + bookingRequest.getTimeSlot() + " is unavailable");
        }

        // Create the booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setTimeSlot(bookingRequest.getTimeSlot());
        booking.setStatus(BookingStatus.PENDING);

        // Remove the booked time slot from the schedule
        schedule.getTimeSlots().remove(bookingRequest.getTimeSlot());
        scheduleRepository.save(schedule); // Save updated schedule

        return bookingRepository.save(booking);
    }




    // Cancel Booking
    public boolean cancelBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (booking.getStatus() != BookingStatus.CANCELLED) {
                booking.setStatus(BookingStatus.CANCELLED);
                bookingRepository.save(booking);

                // Restore the time slot to the schedule
                Schedule schedule = booking.getSchedule();
                schedule.getTimeSlots().add(booking.getTimeSlot());
                scheduleRepository.save(schedule);
                return true;
            }
        }
        return false;
    }

    // Get User Bookings
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking confirmBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (booking.getStatus() == BookingStatus.PENDING) {
                booking.setStatus(BookingStatus.CONFIRMED);
                return bookingRepository.save(booking);
            } else {
                throw new RuntimeException("Booking is already confirmed or cancelled.");
            }
        }
        else {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }
    }
}
