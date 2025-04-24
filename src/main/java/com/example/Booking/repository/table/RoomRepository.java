package com.example.Booking.repository.table;


import com.example.Booking.model.table.Booking;
import com.example.Booking.model.table.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailable(boolean available);

    boolean existsByRoomNumber(String roomNumber);


}