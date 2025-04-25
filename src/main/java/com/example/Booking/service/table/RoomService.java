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

    public List<RoomDto> getAllRooms(Optional<Boolean> available) {

        List<Room> roomDtoList = available
                .map(roomRepository::findByAvailable)
                .orElse(roomRepository.findAll());

        return roomDtoList.stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    public Room getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room with ID " + id + " not found"));
        return room;
    }

//    @Transactional
//    public void updateRoomAvailability(Long id, Boolean available) {
//        Room room = getRoomById(id);
//
//        room.setAvailable(available);
//
//        roomRepository.save(room);
//    }



    public RoomDto CreateRoom(RoomDto roomDto) {
            Room room = roomMapper.toEntity(roomDto);
            room.setAvailable(true);
            Room saved = save(room);
            return roomMapper.toDto(saved);
    }

    public Room save(Room room) {

        try {
//            room.setRoomNumber(null);
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