package com.booking.booking_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Service is required")
    @ManyToOne
    private Service service; // The service this schedule belongs to

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Time slots are required")
    @ElementCollection
    @CollectionTable(name = "schedule_time_slots", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "time_slot")
    private List<String> timeSlots;
}

