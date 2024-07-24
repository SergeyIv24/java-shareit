package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    private final EntityManager em;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private static User expectedUserAlex;
    private static User expectedUserLeonard;

    private static Item expectedItem;

    private static BookingRequest expectedBookingRequest;
    private static BookingRequest futureBookingUserFrom1;
    private static BookingRequest futureBookingUserFrom2;

    private static LocalDateTime now = LocalDateTime.now();

    @BeforeAll
    static void setup() {
        expectedUserAlex = new User();
        expectedUserAlex.setName("Alex");
        expectedUserAlex.setEmail("Alex@Alex.ru");

        expectedUserLeonard = new User();
        expectedUserLeonard.setName("Leonard");
        expectedUserLeonard.setEmail("Leonard@gmail.ru");

        expectedItem = new Item();
        expectedItem.setName("Elephant");
        expectedItem.setDescription("New Elephant");
        expectedItem.setAvailable(true);

        expectedBookingRequest = new BookingRequest();
        expectedBookingRequest.setStart(now);
        expectedBookingRequest.setEnd(now.plusWeeks(10));

        futureBookingUserFrom1 = new BookingRequest();
        futureBookingUserFrom1.setStart(now.plusWeeks(10));
        futureBookingUserFrom1.setEnd(now.plusWeeks(20));

        futureBookingUserFrom2 = new BookingRequest();
        futureBookingUserFrom2.setStart(now.plusWeeks(50));
        futureBookingUserFrom2.setEnd(now.plusWeeks(100));
    }

    @Test
    void shouldReturnBookingAfterCreating() {

        User savedUser1 = userRepository.save(expectedUserAlex);
        User savedUser2 = userRepository.save(expectedUserLeonard);

        expectedItem.setUser(expectedUserAlex);
        Item savedItem = itemRepository.save(expectedItem);

        expectedBookingRequest.setItemId(savedItem.getId());
        BookingDto savedBooking = bookingService.addBookingRequest(savedUser2.getId(), expectedBookingRequest);

        assertThat(savedBooking.getId(), notNullValue());
        assertThat(savedBooking.getBooker().getId(), equalTo(savedUser2.getId()));
        assertThat(savedBooking.getStatus(), equalTo(String.valueOf(BookingStatus.WAITING)));

        BookingDto approveBookingByOwner =
                bookingService.approveBooking(savedBooking.getId(), true, savedUser1.getId());

        BookingDto gotBooking = bookingService.getBooking(savedBooking.getId(), expectedUserAlex.getId());

        assertThat(gotBooking.getId(), equalTo(approveBookingByOwner.getId()));
        assertThat(gotBooking.getStatus(), equalTo(String.valueOf(BookingStatus.APPROVED)));
        assertThat(gotBooking.getBooker(), equalTo(expectedUserLeonard));
        assertThat(gotBooking.getStart(), equalTo(now));
    }

    @Test
    void shouldReturnFutureBookings() {
        User savedUser1 = userRepository.save(expectedUserAlex);
        User savedUser2 = userRepository.save(expectedUserLeonard);

        expectedItem.setUser(savedUser1);
        Item savedItem = itemRepository.save(expectedItem);

        futureBookingUserFrom1.setItemId(savedItem.getId());
        futureBookingUserFrom2.setItemId(savedItem.getId());

        BookingDto savedFutureBooking1 = bookingService.addBookingRequest(savedUser2.getId(), futureBookingUserFrom1);
        BookingDto savedFutureBooking2 = bookingService.addBookingRequest(savedUser2.getId(), futureBookingUserFrom2);

        List<BookingDto> futureBookings = bookingService.getBookingsByConditions(savedUser2.getId(),
                String.valueOf(BookingStates.FUTURE),
                LocalDateTime.now());

        assertThat(futureBookings.size(), equalTo(2));
        assertThat(futureBookings.get(0).getId(), equalTo(savedFutureBooking2.getId()));
        assertThat(futureBookings.get(0).getItem().getName(), equalTo(savedFutureBooking2.getItem().getName()));
        assertThat(futureBookings.get(0).getBooker().getId(), equalTo(savedUser2.getId()));
        assertThat(futureBookings.get(0).getStart(), equalTo(now.plusWeeks(50)));
        assertThat(futureBookings.get(0).getEnd(), equalTo(now.plusWeeks(100)));

        assertThat(futureBookings.get(1).getId(), equalTo(savedFutureBooking1.getId()));
        assertThat(futureBookings.get(1).getItem().getName(), equalTo(savedFutureBooking1.getItem().getName()));
        assertThat(futureBookings.get(1).getBooker().getId(), equalTo(savedUser2.getId()));
        assertThat(futureBookings.get(1).getStart(), equalTo(now.plusWeeks(10)));
        assertThat(futureBookings.get(1).getEnd(), equalTo(now.plusWeeks(20)));

        List<BookingDto> futureBookingsByOwner = bookingService.getBookingsForOwner(savedUser1.getId(),
                String.valueOf(BookingStates.FUTURE),
                LocalDateTime.now());

        assertThat(futureBookingsByOwner.size(), equalTo(2));
        assertThat(futureBookingsByOwner.get(0).getId(), equalTo(savedFutureBooking2.getId()));
        assertThat(futureBookingsByOwner.get(0).getItem().getName(), equalTo(savedFutureBooking2.getItem().getName()));
        assertThat(futureBookingsByOwner.get(0).getBooker().getId(), equalTo(savedUser2.getId()));
        assertThat(futureBookingsByOwner.get(0).getStart(), equalTo(now.plusWeeks(50)));
        assertThat(futureBookingsByOwner.get(0).getEnd(), equalTo(now.plusWeeks(100)));

        assertThat(futureBookingsByOwner.get(1).getId(), equalTo(savedFutureBooking1.getId()));
        assertThat(futureBookingsByOwner.get(1).getItem().getName(), equalTo(savedFutureBooking1.getItem().getName()));
        assertThat(futureBookingsByOwner.get(1).getBooker().getId(), equalTo(savedUser2.getId()));
        assertThat(futureBookingsByOwner.get(1).getStart(), equalTo(now.plusWeeks(10)));
        assertThat(futureBookingsByOwner.get(1).getEnd(), equalTo(now.plusWeeks(20)));
    }
}
