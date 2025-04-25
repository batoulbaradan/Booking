# Booking
Hotel Room Booking API (Spring Boot)
Project Overview
This project is a Room Booking API developed using Java and Spring Boot, designed to manage hotel room reservations. The application provides functionalities such as checking room availability, making bookings, and canceling existing bookings. I designed and implemented all core features, focusing on database management, validation, and RESTful APIs.

## Features:

- View available rooms
- Book a room (with date validation & overlap prevention)
- Cancel bookings
- Simulated email confirmation via Kafka
- Swagger API Documentation

## 🚀 Tech Stack

- Java 17
- Spring Boot 3+
- Spring Data JPA
- Spring Kafka
- H2 In-Memory DB
- Kafka (via Docker)
- Lombok
- Swagger (Springdoc OpenAPI)

## ⚙️ Setup Instructions

### Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose

### 1. Clone the repository
    git clone https://github.com/batoulbaradan/Booking.git

### 2. Start Kafka using Docker Compose
    docker-compose up -d

### 3. Run the application
    ./mvnw spring-boot:run

### 4. Access the API Documentation
    Swagger UI is available at:
    http://localhost:8080/swagger-ui.html

#### My Role
As a backend developer, I was responsible for:

Designing the core business logic for booking and room management.

Building RESTful APIs to handle room reservations and cancellations.

Ensuring data integrity and preventing double bookings by checking for overlapping reservations.

Implementing error handling and validation for incoming requests.

Configuring a database connection (using H2 Database) and implementing Spring Data JPA for data persistence.

##### Key Features and Responsibilities
1. Room Management:
       Developed the RoomService to manage room-related operations, including viewing, creating, and updating room availability.
       Implemented validation to prevent duplicate room numbers and ensure room attributes like capacity and price are correctly defined.

2. Booking Management:
      Created the BookingService to handle booking creation, cancellation, and validation logic.

      Built API endpoints to:

          Create bookings (POST /bookings).

          Cancel bookings (PUT /bookings/{id}/cancel).

          View booking details (GET /bookings/{id}).

          Create room (POST /rooms).

          Retruve rooms with available filter (GET /rooms?available?true).


      Added complex business logic to prevent room double bookings, ensuring that room availability is checked against existing bookings.

      Ensured that the check-out date is after the check-in date.

3. Error Handling

     Implemented a centralized exception handling mechanism using @RestControllerAdvice to catch and return meaningful error messages (e.g., RoomUnavailableException, ResourceNotFoundException, DataIntegrityViolationException).

     Designed a comprehensive error response format to provide users with clear feedback on issues like invalid input, unavailable rooms, or booking conflicts.

4. Validation & Constraints

     Utilized Bean Validation annotations (e.g., @NotNull, @Min, @NotBlank, etc.) to validate request data such as room ID, customer name, and booking dates.

     Applied validation in the BookingService to ensure the booking dates are properly checked before creating new bookings, avoiding conflicts with existing reservations.

5. Database Integration

  Integrated Spring Data JPA for data persistence, using H2 Database for quick development and testing.

  Implemented custom repository methods for complex database queries to handle room availability checks and retrieve booking details.

6. API Documentation

      Added Swagger documentation for the API endpoints, making it easy for developers to test and understand the available API functions.

      Ensured that the endpoints are clear, consistent, and provide appropriate HTTP status codes and responses.


###### Example Requests & Responses
1. Create a Booking:

        Request (POST /bookings):

        json
        {
          "roomId": 1,
          "customerName": "John Doe",
          "checkIn": "2025-05-01",
          "checkOut": "2025-05-05"
        }

        Response:

        json

        {
          "success": true,
          "message": "Booking added successfully",
          "data": {
            "id": 1,
            "roomId": 3,
            "customerName": "John Doe",
            "checkIn": "2025-05-01",
            "checkOut": "2025-05-05",
            "status": "CONFIRMED"
          }
        }

2. Cancel a Booking:
        Request (PUT /bookings/1/cancel):

        Response:

        json

        {
          "success": true,
          "message": "Booking canceled successfully",
          "data": {
            "id": 1,
            "roomId": 3,
            "customerName": "John Doe",
            "checkIn": "2025-05-01",
            "checkOut": "2025-05-05",
            "status": "CANCELLED"
          }
        }

3. Create a Room:

        Request (POST /rooms):

        json
        {
          "roomNumber": "1010A",
          "capacity": 2,
          "pricePerNight": 200.00,
          "available": false
        }

        Response:

        json

        {
            "success": true,
            "message": "Room added successfully",
            "data": {
                "id": 14,
                "roomNumber": "1011A",
                "capacity": 2,
                "pricePerNight": 200.00,
                "available": true
            }
        }

4. Get All Room:
        Request (GET /rooms):

        Response:

        json

        {
            "success": true,
            "message": "Rooms retrieved successfully",
            "data": [
                {
                    "id": 1,
                    "roomNumber": "101A",
                    "capacity": 2,
                    "pricePerNight": 150.00,
                    "available": false
                },
                {
                    "id": 2,
                    "roomNumber": "103A",
                    "capacity": 4,
                    "pricePerNight": 400.00,
                    "available": false
                },
                {
                    "id": 3,
                    "roomNumber": "104A",
                    "capacity": 2,
                    "pricePerNight": 200.00,
                    "available": false
                }
            ]
        }


Challenges & Solutions
Overlapping Bookings: One of the main challenges was ensuring that room bookings didn’t overlap. I implemented a method in the BookingService that checks for overlapping bookings and throws a custom exception (RoomUnavailableException) when necessary.

Room Availability Management: I had to handle dynamic availability of rooms based on bookings, ensuring that once a booking was confirmed, the room's availability was updated.

Error Handling and Validation: Implemented robust error handling and validation mechanisms to ensure the system provides meaningful responses to invalid requests, preventing issues like incorrect room numbers or missing fields.