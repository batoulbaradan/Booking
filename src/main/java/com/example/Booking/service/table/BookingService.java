package com.example.Booking.service.table;


import com.example.Booking.Enum.BookingStatus;
import com.example.Booking.dto.BookingDetailDto;
import com.example.Booking.dto.BookingDto;
import com.example.Booking.exception.BookingAlreadyCancelledException;
import com.example.Booking.exception.ResourceNotFoundException;
import com.example.Booking.exception.RoomUnavailableException;
import com.example.Booking.service.basic.ConfirmationEmailService;
import com.example.Booking.mapper.BookingMapper;
import com.example.Booking.model.table.Booking;
import com.example.Booking.model.table.Room;
import com.example.Booking.repository.table.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final BookingMapper bookingMapper;
    private final ApplicationEventPublisher eventPublisher;

    private final ConfirmationEmailService kafkaEmailProducer;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository, RoomService roomService, BookingMapper bookingMapper, ApplicationEventPublisher eventPublisher, ConfirmationEmailService kafkaEmailProducer) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.bookingMapper = bookingMapper;
        this.eventPublisher = eventPublisher;
        this.kafkaEmailProducer = kafkaEmailProducer;
    }


    //    Retrieves a booking by its ID.
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found"));
    }

    //    Retrieves booking details with room info
    public BookingDetailDto getBookingDetailById(Long id) {
        Booking booking = bookingRepository.findWithRoomById(id).orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found"));
        return bookingMapper.toDetailDto(booking);
    }

    //Creates a new booking if room is available and dates are valid.

    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {
        try {
            Room room = roomService.getRoomById(bookingDto.getRoomId());

            // Prevent overlapping bookings
            validateRoomAvailability(bookingDto);

            Booking booking = bookingMapper.toEntity(bookingDto);
            booking.setRoom(room);
            booking.setStatus(BookingStatus.CONFIRMED);

            Booking savedBooking = bookingRepository.save(booking);

            // Mark room as unavailable
            room.setAvailable(false);
            roomService.save(room);

            // Simulate email confirmation via Kafka
            String message = String.format(
                    "Booking confirmed for %s, Room #%s, from %s to %s",
                    booking.getCustomerName(),
                    booking.getRoom().getRoomNumber(),
                    booking.getCheckIn(),
                    booking.getCheckOut()
            );
            kafkaEmailProducer.sendEmail(message);
//            logEmailConfirmation(savedBooking);
//            eventPublisher.publishEvent(new BookingConfirmedEvent(booking));

            return bookingMapper.toDto(savedBooking);

        } catch (RoomUnavailableException | ResourceNotFoundException | IllegalArgumentException ex) {
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Failed to create booking due to database constraints", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred while creating booking", ex);
        }
    }

    // Validates if the room is available for the requested date range.
    // Also ensures check-in is before check-out.
    private void validateRoomAvailability(BookingDto bookingDto) {

        //use when is there time
        if (!bookingDto.getCheckOut().isAfter(bookingDto.getCheckIn())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        // Check if there are any confirmed bookings that overlap with the requested date range for the same room
        if (bookingRepository.countOverlappingBookings(bookingDto.getRoomId(), bookingDto.getCheckIn(), bookingDto.getCheckOut(),BookingStatus.CONFIRMED)>0) {
            throw new RoomUnavailableException("Booking conflict: Room is already booked for the selected dates.");
        }
    }

    // Cancels an existing booking and updates room availability.
    public BookingDto cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + bookingId + " not found"));

        // if it's status is canceled throw BookingAlreadyCancelledException
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("Booking is already cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(updatedBooking);
    }

    private void logEmailConfirmation(Booking booking) {
        logger.info("Simulated email sent to {} for booking Room #{} from {} to {}", booking.getCustomerName(),
                booking.getId(), booking.getCheckIn(),
                booking.getCheckOut());
    }
}
