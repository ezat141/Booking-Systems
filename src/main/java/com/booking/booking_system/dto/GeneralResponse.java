package com.booking.booking_system.dto;

import lombok.Data;

@Data
public class GeneralResponse<T>{
    private String status;
    private T data;
    private String message;

    public GeneralResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> GeneralResponse<T> success(T data) {
        return new GeneralResponse<T>("success", data, null);
    }

    public static <T> GeneralResponse<T> error(String message) {
        return new GeneralResponse<T>("error", null, message);
    }
}
