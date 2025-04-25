package com.example.Booking.service.table;


import com.example.Booking.Enum.BookingStatus;
import com.example.Booking.dto.BookingDetailDto;
import com.example.Booking.dto.BookingDto;
import com.example.Booking.exception.BookingAlreadyCancelledException;
import com.example.Booking.exception.ResourceNotFoundException;
import com.example.Booking.exception.RoomUnavailableException;
import com.example.Booking.mapper.BookingMapper;
import com.example.Booking.model.table.Booking;
import com.example.Booking.model.table.Room;
import com.example.Booking.repository.table.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final BookingMapper bookingMapper;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository, RoomService roomService, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.bookingMapper = bookingMapper;
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found"));
    }
    public BookingDetailDto getBookingDetailById(Long id) {
        Booking booking = getBookingById(id);
        return bookingMapper.toDetailDto(booking);
    }

//    @Transactional
//    public BookingDto createBooking(BookingDto bookingDto) {
//        Room room = roomService.getRoomById(bookingDto.getRoomId());
//
//        validateRoomAvailability(bookingDto);
//
//        Booking booking = bookingMapper.toEntity(bookingDto);
//        booking.setRoom(room);
//        booking.setStatus(BookingStatus.CONFIRMED);
//
//        try {
//            Booking savedBooking = bookingRepository.save(booking);
//            room.setAvailable(false);
//            roomService.save(room);
////            roomService.updateRoomAvailability(room.getId(), false);
//
//            logEmailConfirmation(savedBooking);
//            return bookingMapper.toDto(savedBooking);
//        } catch (DataIntegrityViolationException ex) {
//            throw new DataIntegrityViolationException("Failed to create booking: " + ex.getMessage());
//        }
//        catch (Exception ex) {
//            throw new RuntimeException("Unexpected error occurred while booking: " + ex.getMessage());
//        }
//    }


    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {
        try {
            Room room = roomService.getRoomById(bookingDto.getRoomId());

            validateRoomAvailability(bookingDto);

            Booking booking = bookingMapper.toEntity(bookingDto);
            booking.setRoom(room);
            booking.setStatus(BookingStatus.CONFIRMED);

            Booking savedBooking = bookingRepository.save(booking);
            System.err.println(savedBooking);
            // Update room availability
            room.setAvailable(false);
            roomService.save(room);

            // Simulate email
            logEmailConfirmation(savedBooking);

            return bookingMapper.toDto(savedBooking);

        } catch (RoomUnavailableException | ResourceNotFoundException  | IllegalArgumentException ex) {
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Failed to create booking due to database constraints", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred while creating booking", ex);
        }
    }


    private void validateRoomAvailability(BookingDto bookingDto) {

            if ( !bookingDto.getCheckOut().isAfter(bookingDto.getCheckIn())){
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        List<Booking> existingBookings = bookingRepository.findByRoomIdAndStatus(
                bookingDto.getRoomId(), BookingStatus.CONFIRMED
        );

        boolean overlaps = existingBookings.stream().anyMatch(existing ->
                bookingDto.getCheckIn().isBefore(existing.getCheckOut()) &&
                        bookingDto.getCheckOut().isAfter(existing.getCheckIn())
        );

        if (overlaps) {
            throw new RoomUnavailableException("Booking conflict: Room is already booked for the selected dates.");
        }
    }

    public BookingDto cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + bookingId + " not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("Booking is already cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(updatedBooking);
    }

    private void logEmailConfirmation(Booking booking) {
        logger.info("Simulated email sent to {} for booking Room #{} from {} to {}", booking.getCustomerName(),
                booking.getId(),booking.getCheckIn(),
                booking.getCheckOut());
    }
}
