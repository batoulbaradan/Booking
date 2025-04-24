package com.example.Booking.repository.table;


import com.example.Booking.Enum.BookingStatus;
import com.example.Booking.model.table.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = "room")
    @Query("SELECT b FROM Booking b")
    List<Booking> findAllByRoom();

    List<Booking> findByRoomIdAndStatus(Long id, BookingStatus bookingStatus);
}