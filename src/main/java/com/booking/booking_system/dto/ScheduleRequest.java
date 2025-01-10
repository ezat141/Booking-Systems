package com.booking.booking_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleRequest {
    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Time slots are required")
    private List<String> timeSlots;
}
