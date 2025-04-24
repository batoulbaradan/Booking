package com.example.Booking.dto;

import com.example.Booking.Enum.BookingStatus;
import com.example.Booking.model.table.Room;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
@Data
public class BookingDto {

    private Long id;

    @NotNull(message = "Room ID must not be null")
    private Long roomId;

    @NotNull(message = "Customer name must not be null")
    private String customerName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-in date must not be null")
    private LocalDate checkIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-out date must not be null")
    private LocalDate checkOut;

    private BookingStatus status;

}
