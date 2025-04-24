package com.example.Booking.mapper;

import com.example.Booking.dto.RoomDto;
import com.example.Booking.model.table.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toDto(Room room);
    Room toEntity(RoomDto roomDto);
}
