package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDto addBookingRequest(Long userId, BookingRequest bookingRequest) {
        validateData(bookingRequest);
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found"));
        Item item = itemRepository
                .findById(bookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        if (item.getUser().getId().equals(userId)) {
            throw new NotFoundException("User can not book own item");
        }


        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available");
        }

        bookingRequest.setBooker(owner);
        bookingRequest.setStatus(String.valueOf(BookingStatus.WAITING));
        return BookingMapper.mapToBookingDto(bookingRepository
                .save(BookingMapper.mapToBooking(bookingRequest, item, owner)));
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Boolean approved, Long ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking is not found"));
        validateBookingApprovement(ownerId, booking);

        if (approved) {
            booking.setStatus(String.valueOf(BookingStatus.APPROVED));
        }

        if (!approved) {
            booking.setStatus(String.valueOf(BookingStatus.REJECTED));
        }
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long ownerId) {
        validateUser(ownerId);
        Booking booking = bookingRepository
                .findById(bookingId).orElseThrow(() -> new NotFoundException("Booking is not found"));
        if ((!booking.getBooker().getId().equals(ownerId))
                && (!booking.getItem().getUser().getId().equals(ownerId))) {
            throw new NotFoundException("Booking does not belong to user");
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByConditions(Long userId, String states, LocalDateTime now) {

        if (states == null) {
            states = "ALL";
        }

        validateState(states);
        validateUser(userId);
        return getBookingsUseStates(userId, BookingStates.valueOf(states), now);
    }

    @Override
    public List<BookingDto> getBookingsForOwner(Long ownerId, String states, LocalDateTime now) {

        if (states == null) {
            states = "ALL";
        }

        validateState(states);
        validateUser(ownerId);
        List<Item> userItems = itemRepository.findByUserId(ownerId);
        if (userItems.isEmpty()) {
            throw new ValidationException("User does not have items");
        }
        return getBookingsUseStatesForOwner(ownerId, BookingStates.valueOf(states), now);

    }

    private List<BookingDto> getBookingsUseStates(Long userId, BookingStates states, LocalDateTime now) {

        List<Booking> bookings = switch (states) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(userId);
            case FUTURE -> bookingRepository
                    .findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case PAST -> bookingRepository
                    .findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case CURRENT -> bookingRepository.findCurrentBookings(userId, now);
            case WAITING, REJECTED ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, String.valueOf(states));
        };

        assert bookings != null;
        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    private List<BookingDto> getBookingsUseStatesForOwner(Long userId, BookingStates states, LocalDateTime now) {

        List<Booking> bookings = switch (states) {
            case ALL -> bookingRepository.findByOwnerIdOrderByStartDesc(userId);
            case FUTURE -> bookingRepository
                    .findByOwnerIdStatusFutureAndOrderByStartDesc(userId, now);
            case PAST -> bookingRepository
                    .findByOwnerIdStatusPastAndOrderByStartDesc(userId, now);
            case CURRENT -> bookingRepository.findByOwnerIdStatusCurrentAndOrderByStartDesc(userId, now);
            case REJECTED -> bookingRepository.findByOwnerIdStatusRejectedAndOrderByStartDesc(userId);
            case WAITING -> bookingRepository.findByOwnerIdStatusWaitingAndOrderByStartDesc(userId);
        };

        assert bookings != null;
        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }


    private void validateData(BookingRequest bookingRequest) {
        if (bookingRequest.getStart().equals(bookingRequest.getEnd())) {
            log.warn("Start equal end");
            throw new ValidationException("Start equal end");
        }
        if (bookingRequest.getEnd().isBefore(bookingRequest.getStart())) {
            log.warn("user tried to return item in the past");
            throw new ValidationException("End is before start");
        }
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found"));
    }

    private void validateState(String state) {
        try {
            BookingStates states = BookingStates.valueOf(state);
        } catch (Throwable e) {
            throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void validateBookingApprovement(Long ownerId, Booking booking) {
        if (booking.getBooker().getId().equals(ownerId)) {
            throw new NotFoundException("Item does not belong to user");
        }

        if (!booking.getItem().getUser().getId().equals(ownerId)) {
            throw new ValidationException("Item does not belong to user");
        }

        if (booking.getStatus().equals(String.valueOf(BookingStatus.APPROVED))) {
            throw new ValidationException("Booking is already approved");
        }
    }
}
