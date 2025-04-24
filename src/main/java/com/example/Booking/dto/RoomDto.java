package com.example.Booking.dto;

import jakarta.persistence.Column;
import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @Min(value = 1, message = "Room capacity must be at least 1")
    private int capacity;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerNight;

    private boolean available;
}
