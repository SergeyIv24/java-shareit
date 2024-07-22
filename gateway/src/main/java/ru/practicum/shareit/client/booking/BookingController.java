package ru.practicum.shareit.client.booking;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.booking.dto.BookingRequest;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addRequestBooking(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                    @Valid @RequestBody BookingRequest bookingRequest) {
        return bookingClient.addBookingRequest(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> bookingConformation(@PathVariable(value = "bookingId") long bookingId,
                                                      @PathParam("approved") Boolean approved,
                                                      @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return bookingClient.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingById(@PathVariable(value = "bookingId") long bookingId,
                                                 @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return bookingClient.getBooking(bookingId, ownerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllBookingsByUser(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                       @PathParam("state") String state) {
        return bookingClient.getBookingsByConditions(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllBookingsForOwner(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                                                         @PathParam("state") String state) {
        return bookingClient.getBookingsForOwner(ownerId, state);
    }
}
