package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithInfoDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        return bookingDto;
    }

    public static Booking mapToBooking(BookingRequest bookingRequest, Item item, User owner) {
        Booking booking = new Booking();
        booking.setId(bookingRequest.getId());
        booking.setStart(bookingRequest.getStart());
        booking.setEnd(bookingRequest.getEnd());
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
        bookingDtoDates.setStart(booking.getStart());
        bookingDtoDates.setEnd(booking.getEnd());
        return bookingDtoDates;
    }
}
