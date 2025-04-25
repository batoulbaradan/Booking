package com.example.Booking.mapper;

import com.example.Booking.dto.BookingDetailDto;
import com.example.Booking.dto.BookingDto;
import com.example.Booking.model.table.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
//    @Mapping(source = "room.id" , target = "roomId")
    BookingDetailDto toDetailDto(Booking booking);

    @Mapping(source = "room.id" , target = "roomId")
    BookingDto toDto(Booking booking);
    @Mapping(target = "room.id" , source = "roomId")
    Booking toEntity(BookingDto bookingDto);

}
