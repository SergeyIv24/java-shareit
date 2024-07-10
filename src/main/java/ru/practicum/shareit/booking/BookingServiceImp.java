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

import java.time.Instant;
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
        validationData(bookingRequest);
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found"));
        Item item = itemRepository
                .findById(bookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Item is not found"));
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available");
        }

        bookingRequest.setBooker(owner);
        bookingRequest.setStatus(String.valueOf(BookingStatus.WAITING));
        return BookingMapper.mapToBookingDto(bookingRepository.save(BookingMapper.mapToBooking(bookingRequest, item, owner)));
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Boolean approved, Long ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking is not found"));

        if (!booking.getItem().getUser().getId().equals(ownerId)) {
            throw new ValidationException("Item does not belong to user");

        }

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
            throw new ValidationException("Booking does not belong to user");
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByConditions(Long userId, String states, Instant now) {

        if (states == null) {
            states = "ALL";
        }

        validateState(states);
        validateUser(userId);
        return getBookingsUseStates(userId, BookingStates.valueOf(states), now);

    }

    @Override
    public List<BookingDto> getBookingsForOwner(Long ownerId, String states, Instant now) {

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

    private List<BookingDto> getBookingsUseStates(Long userId, BookingStates states, Instant now) {

        List<Booking> bookings = switch (states) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(userId);
            case FUTURE -> bookingRepository
                    .findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case PAST -> bookingRepository
                    .findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case CURRENT -> bookingRepository.findCurrentBookings(userId);
            case WAITING, REJECTED ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, String.valueOf(states));
        };

        assert bookings != null;
        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    private List<BookingDto> getBookingsUseStatesForOwner(Long userId, BookingStates states, Instant now) {

        List<Booking> bookings = switch (states) {
            case ALL -> bookingRepository.findByOwnerIdOrderByStartDesc(userId);
            case FUTURE -> bookingRepository
                    .findByOwnerIdStatusFutureAndOrderByStartDesc(userId, now);
            case PAST -> bookingRepository
                    .findByOwnerIdStatusPastAndOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findByOwnerIdStatusCurrentAndOrderByStartDesc(userId);
            case REJECTED -> bookingRepository.findByOwnerIdStatusRejectedAndOrderByStartDesc(userId);
            case WAITING -> bookingRepository.findByOwnerIdStatusWaitingAndOrderByStartDesc(userId);
        };

        assert bookings != null;
        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }


    private void validationData(BookingRequest bookingRequest) {
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
}
