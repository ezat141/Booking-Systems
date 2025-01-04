package com.booking.booking_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    @NotNull(message = "Time slot is required")
    private String timeSlot;


}
