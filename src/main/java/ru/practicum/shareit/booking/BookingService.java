package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto addBookingRequest(Long userId, BookingRequest bookingRequest);

    BookingDto approveBooking(Long bookingId, Boolean approved);

    BookingDto getBooking(Long bookingId);

    List<BookingDto> getBookingsByConditions(Long userId, BookingStates states, Instant now);

    List<BookingDto> getBookingsForOwner(Long ownerId, BookingStates states, Instant now);
}
