package com.booking.booking_system.dto;

import java.util.List;

public class TimeSlotRequest {
    private List<String> timeSlots;

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<String> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
