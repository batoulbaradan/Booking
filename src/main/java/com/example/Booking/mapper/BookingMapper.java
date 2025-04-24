package com.example.Booking.mapper;

import com.example.Booking.dto.BookingDto;
import com.example.Booking.dto.RoomDto;
import com.example.Booking.model.table.Booking;
import com.example.Booking.model.table.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
//    @Mapping(target = "checkIn",expression = "java(java.time.LocalDate)")
    @Mapping(source = "room.id" , target = "roomId")
    BookingDto toDto(Booking booking);
    @Mapping(target = "room.id" , source = "roomId")
    Booking toEntity(BookingDto bookingDto);

}
