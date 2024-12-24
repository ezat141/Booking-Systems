package com.booking.booking_system.utils;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
