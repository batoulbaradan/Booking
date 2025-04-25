package com.example.Booking.repository.table;


import com.example.Booking.Enum.BookingStatus;
import com.example.Booking.model.table.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = "room")
    @Query("SELECT b FROM Booking b")
    List<Booking> findAllByRoom();

    @EntityGraph(attributePaths = "room")
    Optional<Booking> findWithRoomById(Long id);

//    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.room.id = :roomId AND b.status = 'CONFIRMED' AND " +
//            "(:checkIn < b.checkOut AND :checkOut > b.checkIn)")
//    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
//                                     @Param("checkIn") LocalDate checkIn,
//                                     @Param("checkOut") LocalDate checkOut);

    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.status = :status " +
            "AND (:checkIn < b.checkOut AND :checkOut > b.checkIn)")
    Long countOverlappingBookings(@Param("roomId") Long roomId,
                                  @Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut,
                                  @Param("status") BookingStatus status);
    List<Booking> findByRoomIdAndStatus(Long id, BookingStatus bookingStatus);
}