package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto addBookingRequest(Long userId, BookingRequest bookingRequest);

    BookingDto approveBooking(Long bookingId, Boolean approved, Long ownerId);

    BookingDto getBooking(Long bookingId, Long ownerId);

    List<BookingDto> getBookingsByConditions(Long userId, String states, LocalDateTime now);

    List<BookingDto> getBookingsForOwner(Long ownerId, String states, LocalDateTime now);
}
