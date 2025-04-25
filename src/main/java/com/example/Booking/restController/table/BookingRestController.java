package com.example.Booking.restController.table;


import com.example.Booking.dto.BookingDetailDto;
import com.example.Booking.dto.BookingDto;
import com.example.Booking.response.ApiResponse;
import com.example.Booking.service.table.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingRestController {

    private final BookingService bookingService;

    public BookingRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDetailDto>> getBookingById(@PathVariable Long id) {
        BookingDetailDto bookingDTO = bookingService.getBookingDetailById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Booking retrieved successfully", bookingDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingDto>> addBooking(@Valid @RequestBody BookingDto bookingDto) {
        BookingDto savedBooking = bookingService.createBooking(bookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Booking added successfully", savedBooking));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingDto>> cancelBooking(@PathVariable Long id) {
        BookingDto bookingDto = bookingService.cancelBooking(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Booking canceled successfully", bookingDto));
    }

}

