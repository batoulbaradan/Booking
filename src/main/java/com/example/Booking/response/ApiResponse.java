package com.example.Booking.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

//Generic API response wrapper to standardize the format of HTTP responses
@Getter
@Setter
public class ApiResponse<T> {
//    Indicates whether the operation was successful or not.
    private boolean success;

//    A message giving more context about the operation result.
    private String message;

//    Optional data payload returned in the response.
//    Only included if not null to reduce response size
    @JsonInclude(JsonInclude.Include.NON_NULL) // Include only if not null
    @JsonProperty("data")
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(boolean success, String message) {
        this(success, message, null);
    }

}
