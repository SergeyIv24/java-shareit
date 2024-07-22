package ru.practicum.shareit.booking;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BookingDto addRequestBooking(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                        @RequestBody BookingRequest bookingRequest) {
        return bookingService.addBookingRequest(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto bookingConformation(@PathVariable(value = "bookingId") long bookingId,
                                          @PathParam("approved") Boolean approved,
                                          @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return bookingService.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@PathVariable(value = "bookingId") long bookingId,
                                     @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return bookingService.getBooking(bookingId, ownerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsByUser(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                 @PathParam("state") String state) {
        return bookingService.getBookingsByConditions(userId, state, LocalDateTime.now());
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                                                   @PathParam("state") String state) {
        return bookingService.getBookingsForOwner(ownerId, state, LocalDateTime.now());
    }
}
