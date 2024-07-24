package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static BookingRequest bookingRequest1;
    private static BookingDto bookingDto2;
    private static BookingDto bookingDto3;
    private static BookingDto bookingDto4;
    private static Item item1;
    private static User user1;
    private static User user2;
    private static final LocalDateTime time = LocalDateTime
            .of(2024, 7, 22, 17, 12, 12, 12);

    @BeforeAll
    static void setup() {
        bookingRequest1 = new BookingRequest();
        bookingRequest1.setItemId(1L);
        bookingRequest1.setStart(time.plusWeeks(1));
        bookingRequest1.setEnd(time.plusWeeks(2));

        bookingDto2 = new BookingDto();
        bookingDto2.setId(2L);
        bookingDto2.setStart(time.plusWeeks(2));
        bookingDto2.setEnd(time.plusWeeks(4));
        bookingDto2.setItem(item1);

        bookingDto3 = new BookingDto();
        bookingDto3.setId(3L);
        bookingDto3.setStart(time.plusWeeks(5));
        bookingDto3.setEnd(time.plusWeeks(6));
        bookingDto3.setItem(item1);

        bookingDto4 = new BookingDto();
        bookingDto4.setId(4L);
        bookingDto4.setStart(time.plusWeeks(7));
        bookingDto4.setEnd(time.plusWeeks(8));
        bookingDto4.setItem(item1);

        user1 = new User();
        user1.setId(1L);
        user1.setName("Test");
        user1.setEmail("Testov@Test.test");

        user2 = new User();
        user2.setId(2L);
        user2.setName("Test");
        user2.setEmail("TestUser@User.ru");

        item1 = new Item();
        item1.setId(1L);
        item1.setName("Test Item");
        item1.setDescription("1111");
        item1.setUser(user1);
        item1.setAvailable(true);
    }

    @Test
    void shouldAddRequestBooking() throws Exception {
        BookingDto bookingDto1 = BookingMapper
                .mapToBookingDto(BookingMapper
                        .mapToBooking(bookingRequest1, item1, user1));

        when(bookingService.addBookingRequest(anyLong(), any()))
                .thenReturn(bookingDto1);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequest1))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingRequest1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingRequest1.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingRequest1.getEnd().toString())));

    }

    @Test
    void shouldBeConfirmed() throws Exception {
        BookingDto bookingDto1 = BookingMapper
                .mapToBookingDto(BookingMapper
                        .mapToBooking(bookingRequest1, item1, user1));
        when(bookingService.approveBooking(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingDto1);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().toString())));
    }

    @Test
    void shouldReturnBookingById() throws Exception {
        BookingDto bookingDto1 = BookingMapper
                .mapToBookingDto(BookingMapper
                        .mapToBooking(bookingRequest1, item1, user1));

        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto1);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().toString())));
    }

    @Test
    void shouldReturnAllBookingsForUser() throws Exception {
        BookingDto bookingDto1 = BookingMapper
                .mapToBookingDto(BookingMapper
                        .mapToBooking(bookingRequest1, item1, user1));

        bookingDto1.setBooker(user2);
        bookingDto2.setBooker(user2);
        bookingDto3.setBooker(user2);
        bookingDto4.setBooker(user2);

        when(bookingService.getBookingsByConditions(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDto1, bookingDto2, bookingDto3, bookingDto4));

        mvc.perform(get("/bookings?state=FUTURE")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookingDto1.getEnd().toString())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[1].id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].start", is(bookingDto2.getStart().toString())))
                .andExpect(jsonPath("$.[1].end", is(bookingDto2.getEnd().toString())))
                .andExpect(jsonPath("$.[1].booker.id", is(bookingDto2.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[2].id", is(bookingDto3.getId()), Long.class))
                .andExpect(jsonPath("$.[2].start", is(bookingDto3.getStart().toString())))
                .andExpect(jsonPath("$.[2].end", is(bookingDto3.getEnd().toString())))
                .andExpect(jsonPath("$.[2].booker.id", is(bookingDto3.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[3].id", is(bookingDto4.getId()), Long.class))
                .andExpect(jsonPath("$.[3].start", is(bookingDto4.getStart().toString())))
                .andExpect(jsonPath("$.[3].end", is(bookingDto4.getEnd().toString())))
                .andExpect(jsonPath("$.[3].booker.id", is(bookingDto4.getBooker().getId()), Long.class));
    }

    @Test
    void shouldReturnAllBookingsForOwner() throws Exception {

        bookingDto2.setStart(time.minusWeeks(2));
        bookingDto2.setEnd(time.minusWeeks(1));

        bookingDto3.setStart(time.minusWeeks(10));
        bookingDto3.setEnd(time.minusWeeks(9));

        bookingDto4.setStart(time.minusWeeks(20));
        bookingDto4.setEnd(time.minusWeeks(19));

        bookingDto2.setBooker(user2);
        bookingDto3.setBooker(user2);
        bookingDto4.setBooker(user2);

        when(bookingService.getBookingsForOwner(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDto2, bookingDto3, bookingDto4));

        mvc.perform(get("/bookings/owner?state=PAST")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto2.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDto2.getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookingDto2.getEnd().toString())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDto2.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[1].id", is(bookingDto3.getId()), Long.class))
                .andExpect(jsonPath("$.[1].start", is(bookingDto3.getStart().toString())))
                .andExpect(jsonPath("$.[1].end", is(bookingDto3.getEnd().toString())))
                .andExpect(jsonPath("$.[1].booker.id", is(bookingDto3.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[2].id", is(bookingDto4.getId()), Long.class))
                .andExpect(jsonPath("$.[2].start", is(bookingDto4.getStart().toString())))
                .andExpect(jsonPath("$.[2].end", is(bookingDto4.getEnd().toString())))
                .andExpect(jsonPath("$.[2].booker.id", is(bookingDto4.getBooker().getId()), Long.class));
    }

}
