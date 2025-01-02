package com.booking.booking_system.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private Service service; // The service this schedule belongs to

    @Column(nullable = false)
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "schedule_time_slots", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "time_slot")
    private List<String> timeSlots;
}

