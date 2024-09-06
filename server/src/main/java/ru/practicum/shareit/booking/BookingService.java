package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto addBookingRequest(long userId, BookingRequest bookcingRequest);

    BookingDto approveBooking(long bookingId, Boolean approved, long ownerId);

    BookingDto getBooking(long bookingId, long ownerId);

    List<BookingDto> getBookingsByConditions(long userId, String states, LocalDateTime now);

    List<BookingDto> getBookingsForOwner(long ownerId, String states, LocalDateTime now);
}
