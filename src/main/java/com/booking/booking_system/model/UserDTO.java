package com.booking.booking_system.model;

public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    // No refreshToken to avoid circular reference
}
