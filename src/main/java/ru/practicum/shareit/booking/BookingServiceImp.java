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
    public BookingDto approveBooking(Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking bi not found"));
        if (approved) {
            booking.setStatus(String.valueOf(BookingStatus.APPROVED));
        }

        if (!approved) {
            booking.setStatus(String.valueOf(BookingStatus.REJECTED));
        }
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    public BookingDto getBooking(Long bookingId) {
        return BookingMapper
                .mapToBookingDto(bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new NotFoundException("Booking is not found")));
    }

    @Override
    public List<BookingDto> getBookingsByConditions(Long userId, BookingStates states, Instant now) {
        validateUser(userId);
        return getBookingsUseStates(userId, states, now);

    }

    public List<BookingDto> getBookingsForOwner(Long ownerId, BookingStates states, Instant now) {
        validateUser(ownerId);
        List<Item> userItems = itemRepository.findByUserId(ownerId);
        if (userItems.isEmpty()) {
            throw new ValidationException("User does not have items");
        }
        return getBookingsUseStatesForOwner(ownerId, states, now);

    }

    private List<BookingDto> getBookingsUseStates(Long userId, BookingStates states, Instant now) {
        if (states == null) {
            states = BookingStates.ALL;
        }

        List<Booking> bookings;

        switch (states) {
            case ALL:
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case PAST:
                bookings = bookingRepository
                        .findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentBookings(userId);
                break;
            case WAITING, REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, String.valueOf(states));
                break;
            default:
                throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
        assert bookings != null;
        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    private List<BookingDto> getBookingsUseStatesForOwner(Long userId, BookingStates states, Instant now) {
        if (states == null) {
            states = BookingStates.ALL;
        }

        List<Booking> bookings;

        switch (states) {
            case ALL:
                bookings = bookingRepository.findByOwnerIdOrderByStartDesc(userId);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case PAST:
                bookings = bookingRepository
                        .findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentBookings(userId);
                break;
            case REJECTED:
                bookings = bookingRepository.findByOwnerIdStatusRejectedAndOrderByStartDesc(userId);
                break;
            case WAITING:
                bookings = bookingRepository.findByOwnerIdStatusWaitingAndOrderByStartDesc(userId);
                break;
            default:
                throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
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
}
