package com.booking.booking_system.model;

import com.booking.booking_system.entities.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;
    @NotNull(message = "Time slot is required")
    private String timeSlot;


}
