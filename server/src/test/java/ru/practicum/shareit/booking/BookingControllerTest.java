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
    private static Item item1;
    private static User user1;
    private static final LocalDateTime time = LocalDateTime
            .of(2024, 7, 22, 17, 12, 12, 12);

    @BeforeAll
    static void createRequests() {
        bookingRequest1 = new BookingRequest();
        bookingRequest1.setItemId(1L);
        bookingRequest1.setStart(time.plusWeeks(1));
        bookingRequest1.setEnd(time.plusWeeks(2));
    }

    @BeforeAll
    static void createItemsAndUsers() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("Test");
        user1.setEmail("Testov@Test.test");

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

    }

    @Test
    void shouldReturnAllBookingsForOwner() throws Exception {

    }

}
