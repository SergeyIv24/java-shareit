package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoDates;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class BookingMapper {

    private static final ZoneId zoneUTC0 = ZoneId.of("UTC+3");

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneId.of("Europe/Moscow")));
        bookingDto.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.of("Europe/Moscow")));
        return bookingDto;
    }

    public static Booking mapToBooking(BookingDto bookingDto, Item item, User user) {
        ZoneOffset startOffset = zoneUTC0.getRules().getOffset(bookingDto.getStart());
        ZoneOffset endOffset = zoneUTC0.getRules().getOffset(bookingDto.getEnd());

        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart().toInstant(startOffset));
        booking.setEnd(bookingDto.getEnd().toInstant(endOffset));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static Booking mapToBooking(BookingRequest bookingRequest, Item item, User owner) {
        ZoneOffset startOffset = zoneUTC0.getRules().getOffset(bookingRequest.getStart());
        ZoneOffset endOffset = zoneUTC0.getRules().getOffset(bookingRequest.getEnd());
        Booking booking = new Booking();
        booking.setId(bookingRequest.getId());
        booking.setStart(bookingRequest.getStart().toInstant(startOffset));
        booking.setEnd(bookingRequest.getEnd().toInstant(endOffset));
        booking.setItem(item);
        booking.setBooker(owner);
        booking.setStatus(bookingRequest.getStatus());
        return booking;
    }

    public static BookingDtoDates mapToBookingDtoDates(Booking booking) {
        BookingDtoDates bookingDtoDates = new BookingDtoDates();
        bookingDtoDates.setId(booking.getId());
        bookingDtoDates.setItem(booking.getItem());
        bookingDtoDates.setBookerId(booking.getBooker().getId());
        bookingDtoDates.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneId.of("Europe/Moscow")));
        bookingDtoDates.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.of("Europe/Moscow")));
        return bookingDtoDates;
    }
}
