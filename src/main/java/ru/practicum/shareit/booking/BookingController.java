package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.Instant;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BookingDto addRequestBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                        @Valid @RequestBody BookingRequest bookingRequest) {
        return bookingService.addBookingRequest(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto bookingConformation(@PathVariable(value = "bookingId") Long bookingId,
                                                   @PathParam("approved") Boolean approved,
                                          @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return bookingService.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@PathVariable(value = "bookingId") Long bookingId,
                                     @RequestHeader (value = "X-Sharer-User-Id") Long ownerId) {
        return bookingService.getBooking(bookingId, ownerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsByUser(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                 @PathParam("state") String state) {
        return bookingService.getBookingsByConditions(userId, state, Instant.now());
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                   @PathParam("state") String state) {
        return bookingService.getBookingsForOwner(ownerId, state, Instant.now());
    }
}
