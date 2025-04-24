package com.example.Booking.model.table;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    Long id;
    @Column(name = "room_number", nullable = false,unique = true)
    String roomNumber;
    @Column(name = "capacity", nullable = false)
    int capacity;
    @Column(name = "price_per_night", nullable = false)
    BigDecimal pricePerNight;
    @Column(name = "available", nullable = false)
    Boolean available;


    @PrePersist
    public void prePersist() {
        if (available == null) available = true;
    }
}
