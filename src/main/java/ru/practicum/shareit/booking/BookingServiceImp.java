package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

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
        bookingRequest.setBooker(owner);
        bookingRequest.setStatus(String.valueOf(BookingStatus.WAITING));
        return BookingMapper.mapToBookingDto(bookingRepository.save(BookingMapper.mapToBooking(bookingRequest, item, owner)));
    }





    public BookingDto getBooking(Long bookingId) {
        return BookingMapper
                .mapToBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking is not found")));

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
}
