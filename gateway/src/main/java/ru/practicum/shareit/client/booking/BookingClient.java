package ru.practicum.shareit.client.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.booking.dto.BookingRequest;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build());
    }

    public ResponseEntity<Object> addBookingRequest(long userId, BookingRequest bookingRequest) {
        return post("", userId, bookingRequest);
    }

    public ResponseEntity<Object> approveBooking(long bookingId, boolean approved, long ownerId) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters, null);
    }

    public ResponseEntity<Object> getBooking(long bookingId, long ownerId) {
        return get("/" + bookingId, ownerId);
    }

    public ResponseEntity<Object> getBookingsByConditions(long userId, String state) {
        if (state == null) {
            state = "ALL";
        }
        Map<String, Object> parameters = Map.of("state", state);
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsForOwner(long ownerId, String state) {
        if (state == null) {
            state = "ALL";
        }
        Map<String, Object> parameters = Map.of("state", state);
        return get("/owner?state={state}", ownerId, parameters);
    }
}
