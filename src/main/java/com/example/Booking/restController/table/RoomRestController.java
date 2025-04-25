package com.example.Booking.restController.table;


import com.example.Booking.dto.RoomDto;
import com.example.Booking.response.ApiResponse;
import com.example.Booking.service.table.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {


    final private RoomService roomService;

    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    //Retrieves all rooms, optionally filtered by availability.
    //return ResponseEntity with list of RoomDto wrapped in ApiResponse
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomDto>>> getAllRooms(@RequestParam(required = false,name = "available" ) Boolean available) {
        List<RoomDto> books = roomService.getAllRooms(Optional.ofNullable(available));
        return ResponseEntity.ok(new ApiResponse<>(true, "Rooms retrieved successfully", books));
    }

    //Creates a new room and marks it as available by default.
    //return ResponseEntity with created RoomDto wrapped in ApiResponse
    @PostMapping
    public ResponseEntity<ApiResponse<RoomDto>> addRoom(@Valid @RequestBody RoomDto roomDto) {
        RoomDto savedRoom = roomService.CreateRoom(roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Room added successfully", savedRoom));
    }

}

