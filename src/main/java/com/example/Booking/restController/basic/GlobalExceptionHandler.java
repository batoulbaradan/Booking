package com.example.Booking.restController.basic;

import com.example.Booking.Enum.ErrorMessages;
import com.example.Booking.exception.*;
import com.example.Booking.response.ApiResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    Handles validation errors from @Valid annotated request bodies.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.VALIDATION_FAILED, errors));
    }

//  Handles violations like unique constraints (duplicate room numbers).
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.DATA_INTEGRITY_VIOLATION, ex.getRootCause().getMessage()));
    }
//    Handles conflicts due to room already being booked.
    @ExceptionHandler(RoomUnavailableException.class)
    public ResponseEntity<ApiResponse<String>> handleRoomUnavailableException(RoomUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, ErrorMessages.ROOM_UNAVAILABLE, ex.getMessage()));
    }

//    Handles cases where a requested resource is not found.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ErrorMessages.RESOURCE_NOT_FOUND, ex.getMessage()));
    }

//    Handles illegal or inappropriate argument usage.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.ILLEGAL_ARGUMENT, ex.getMessage()));
    }

//    Handles missing path variable in URI.
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingPathVariableException(MissingPathVariableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.MISSING_PATH_VARIABLE, ex.getVariableName()));
    }

//    Catches unhandled RuntimeExceptions as a fallback.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, ErrorMessages.UNEXPECTED_ERROR, ex.getMessage()));
    }

//    Catches any other general exceptions not previously handled.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, ErrorMessages.GENERIC_ERROR, ex.getMessage()));
    }

//    Handles low-level database access exceptions.
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleDatabaseException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, ErrorMessages.DATABASE_ERROR, ex.getMessage()));
    }

//    Handles duplicate room number violations.
    @ExceptionHandler(DuplicateRoomNException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateRoomNException(DuplicateRoomNException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.DUPLICATE_ROOM_NUMBER, ex.getMessage()));
    }

//    Handles failed login attempts due to incorrect credentials.
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, ErrorMessages.INVALID_CREDENTIALS, ex.getMessage()));
    }

//    Handles type mismatch in request parameters ( passing a string where a number is expected).
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid parameter: " + ex.getName() + " should be of type " + ex.getRequiredType().getSimpleName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.INVALID_PARAMETER, errorMessage));
    }

//    Handles attempts to cancel a booking that was already cancelled.
    @ExceptionHandler(BookingAlreadyCancelledException.class)
    public ResponseEntity<ApiResponse<String>> handleBookingAlreadyCancelledException(BookingAlreadyCancelledException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.CANCEL_BOOKING_ERROR, ex.getMessage()));
    }

//    Handles invalid JSON/date formats in request body.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ErrorMessages.INVALID_DATE_FORMAT, null));
    }
}


