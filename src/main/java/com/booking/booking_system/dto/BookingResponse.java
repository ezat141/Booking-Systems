package com.booking.booking_system.dto;

import com.booking.booking_system.entities.BookingStatus;
import lombok.Data;

@Data
public class BookingResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long scheduleId;
    private String timeSlot;
    private BookingStatus status;
    private Long serviceId;         // Service ID
    private String serviceName;
}
