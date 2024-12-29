package com.booking.booking_system.services;

import com.booking.booking_system.entities.Booking;
import com.booking.booking_system.entities.BookingStatus;
import com.booking.booking_system.entities.User;
import com.booking.booking_system.model.BookingRequest;
import com.booking.booking_system.repositories.BookingRepository;
import com.booking.booking_system.repositories.ServiceRepository;
import com.booking.booking_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ServiceRepository serviceRepository;


    // Get All Bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Create a New Booking
    public Booking createBooking(Booking booking) {
        booking.setStatus(BookingStatus.PENDING); // Default status for new bookings
        return bookingRepository.save(booking);
    }

    // Update an Existing Booking
    public Booking updateBooking(Long id, BookingRequest updatedBooking) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    // Retrieve User by ID
                    User user = userRepository.findById(updatedBooking.getUserId())
                                    .orElseThrow(() -> new RuntimeException("User not found"));
                    // Retrieve Service by ID
                    com.booking.booking_system.entities.Service service = serviceRepository.findById(updatedBooking.getServiceId())
                            .orElseThrow(() -> new RuntimeException("Service not found"));
                    // Update Booking fields
                    booking.setUser(user);
                    booking.setService(service);
                    booking.setTimeSlot(updatedBooking.getTimeSlot());
                    booking.setStatus(updatedBooking.getStatus());
//                    booking.setService(updatedBooking.getService());
//                    booking.setStatus(updatedBooking.getStatus());
//                    booking.setUser(updatedBooking.getUser());

                    // Save updated booking
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }


    // Cancel Booking
    public boolean cancelBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (!"CANCELLED".equalsIgnoreCase(String.valueOf(booking.getStatus()))){
                booking.setStatus(BookingStatus.valueOf("CANCELLED"));
                bookingRepository.save(booking);
                return true;
            }
        }
        return false;
    }

    // Get User Bookings
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
