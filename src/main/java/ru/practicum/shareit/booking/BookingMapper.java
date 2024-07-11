package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithInfoDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    private static final ZoneId zoneUTC0 = ZoneId.of("UTC+3"); //Needs to return date with Moscow time zone

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        //Converting Instant without timezone to LocalDateTime with Moscow Zone
        bookingDto.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneId.of("Europe/Moscow")));
        bookingDto.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.of("Europe/Moscow")));
        return bookingDto;
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

    public static BookingWithInfoDto mapToBookingDtoDates(Booking booking) {
        BookingWithInfoDto bookingDtoDates = new BookingWithInfoDto();
        bookingDtoDates.setId(booking.getId());
        bookingDtoDates.setItem(booking.getItem());
        bookingDtoDates.setBookerId(booking.getBooker().getId());
        bookingDtoDates.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneId.of("Europe/Moscow")));
        bookingDtoDates.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.of("Europe/Moscow")));
        return bookingDtoDates;
    }
}
