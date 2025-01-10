package com.booking.booking_system.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private List<String> timeSlots;
}
