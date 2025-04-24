package com.example.Booking.model.table;

import com.example.Booking.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "customer_name", nullable = false)
    private String customerName;
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;
    @Column(name = "check_out")
    private LocalDate checkOut;
    @Column(name = "status", nullable = false)
    private BookingStatus status;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_id",nullable = false)
    private Room room;
}