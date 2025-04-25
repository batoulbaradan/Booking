package com.example.Booking.dto;

import com.example.Booking.Enum.BookingStatus;
import com.example.Booking.model.table.Room;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class BookingDetailDto {
    private Long id;
    private Room room;
    private String customerName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;
    private BookingStatus status;
}
