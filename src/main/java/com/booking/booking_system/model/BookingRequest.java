package com.booking.booking_system.model;

import com.booking.booking_system.entities.BookingStatus;
import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private Long serviceId;
    private String timeSlot;
    private BookingStatus status;


}
