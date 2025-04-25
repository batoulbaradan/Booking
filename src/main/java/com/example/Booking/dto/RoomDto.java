package com.example.Booking.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class RoomDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @Min(value = 1, message = "Room capacity must be at least 1")
    private int capacity;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerNight;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean available;
}
