package com.booking.booking_system.services;

import com.booking.booking_system.dto.BookingResponse;
import com.booking.booking_system.entities.Booking;
import com.booking.booking_system.entities.BookingStatus;
import com.booking.booking_system.entities.Schedule;
import com.booking.booking_system.entities.User;
import com.booking.booking_system.dto.BookingRequest;
import com.booking.booking_system.exceptions.CustomException;
import com.booking.booking_system.repositories.BookingRepository;
import com.booking.booking_system.repositories.ScheduleRepository;
import com.booking.booking_system.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Service
public class BookingService implements BookingServiceInt{

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public BookingService(BookingRepository bookingRepository, ModelMapper modelMapper, UserRepository userRepository, ScheduleRepository scheduleRepository) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // Get All Bookings
//    public List<Booking> getAllBookings() {
//        return bookingRepository.findAll();
//    }

    // Get All Bookings pages
    public Page<BookingResponse> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(this::mapToBookingResponse);
    }

    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException("Booking not found with ID: " + bookingId));
        return mapToBookingResponse(booking);
    }



    @Transactional
    // Create a New Booking
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        // Retrieve User and Schedule
        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Find the schedule for the given service and time slot
        Schedule schedule = scheduleRepository.findByServiceIdAndTimeSlot(
                        bookingRequest.getServiceId(), bookingRequest.getTimeSlot())
                .orElseThrow(() -> new RuntimeException("No available schedule for the given time slot"));



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
        // Save the booking
        Booking savedBooking= bookingRepository.save(booking);

        return mapToBookingResponse(savedBooking);

    }




    @Transactional
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
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(booking -> modelMapper.map(booking, BookingResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse confirmBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (booking.getStatus() == BookingStatus.PENDING) {
                booking.setStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
                return mapToBookingResponse(booking);
            } else {
                throw new CustomException("Booking is already confirmed or cancelled.");
            }
        }
        else {
            throw new CustomException("Booking not found with ID: " + bookingId);
        }
    }
    private BookingResponse mapToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUser().getId());
        response.setUserName(booking.getUser().getName());
        response.setScheduleId(booking.getSchedule().getId());
        response.setTimeSlot(booking.getTimeSlot());
        response.setStatus(booking.getStatus());
        response.setServiceId(booking.getSchedule().getService().getId());
        response.setServiceName(booking.getSchedule().getService().getName());
        return response;
    }
}
