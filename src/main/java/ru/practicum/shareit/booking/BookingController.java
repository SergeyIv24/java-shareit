package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.Instant;
import java.time.LocalDateTime;
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
                                                   @PathParam("approved") Boolean approved) {
        return bookingService.approveBooking(bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@PathVariable(value = "bookingId") Long bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsByUser(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                 @PathParam("state") BookingStates state) {
        //validateState(state);
        return bookingService.getBookingsByConditions(userId, state, Instant.now());
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                   @PathParam("state") BookingStates states) {
        //validateState(states);
        return bookingService.getBookingsForOwner(ownerId, states, Instant.now());
    }

/*    private void validateState(String state) {
        try {
            BookingStates states = BookingStates.valueOf(state);
        } catch (Throwable e) {
            throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
    }*/



}
