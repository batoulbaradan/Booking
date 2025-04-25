package com.example.Booking.service.table;


import com.example.Booking.dto.RoomDto;
import com.example.Booking.exception.DuplicateRoomNException;
import com.example.Booking.exception.ResourceNotFoundException;
import com.example.Booking.mapper.RoomMapper;
import com.example.Booking.model.table.Room;
import com.example.Booking.repository.table.RoomRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    //Retrieves a list of rooms, optionally filtered by availability.
    public List<RoomDto> getAllRooms(Optional<Boolean> available) {

        List<Room> roomDtoList = available
                .map(roomRepository::findByAvailable)// filter if available is present
                .orElse(roomRepository.findAll()); // otherwise return all rooms

        return roomDtoList.stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetches a room by its ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room with ID " + id + " not found"));
    }

    //Creates a new room and marks it as available by default.
    public RoomDto CreateRoom(RoomDto roomDto) {
            Room room = roomMapper.toEntity(roomDto);
            room.setAvailable(true);
            Room saved = save(room);
            return roomMapper.toDto(saved);
    }

    // Saves a room entity to the database with validation.
    // Handles duplicate room number constraint (unique).
    public Room save(Room room) {

        try {
          return roomRepository.save(room);
        } catch (DataIntegrityViolationException ex) {
            Throwable root = ex.getRootCause();
            if (root != null && root.getMessage().contains("room_number")) {
                throw new DuplicateRoomNException("Room with number " + room.getRoomNumber() + " already exists.");
            }
            throw new DataIntegrityViolationException("Failed to save room: " + ex.getMessage());
        }
    }

}