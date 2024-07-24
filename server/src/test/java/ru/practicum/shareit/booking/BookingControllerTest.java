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

    private static BookingRequest expectedBookingRequest;
    private static BookingDto bookingDtoId2;
    private static BookingDto bookingDtoId3;
    private static BookingDto bookingDtoId4;
    private static Item expectedItem;
    private static User expectedUser;
    private static User expectedAnotherUser;
    private static final LocalDateTime time = LocalDateTime
            .of(2024, 7, 22, 17, 12, 12, 12);

    @BeforeAll
    static void setup() {
        expectedBookingRequest = new BookingRequest();
        expectedBookingRequest.setItemId(1L);
        expectedBookingRequest.setStart(time.plusWeeks(1));
        expectedBookingRequest.setEnd(time.plusWeeks(2));

        bookingDtoId2 = new BookingDto();
        bookingDtoId2.setId(2L);
        bookingDtoId2.setStart(time.plusWeeks(2));
        bookingDtoId2.setEnd(time.plusWeeks(4));
        bookingDtoId2.setItem(expectedItem);

        bookingDtoId3 = new BookingDto();
        bookingDtoId3.setId(3L);
        bookingDtoId3.setStart(time.plusWeeks(5));
        bookingDtoId3.setEnd(time.plusWeeks(6));
        bookingDtoId3.setItem(expectedItem);

        bookingDtoId4 = new BookingDto();
        bookingDtoId4.setId(4L);
        bookingDtoId4.setStart(time.plusWeeks(7));
        bookingDtoId4.setEnd(time.plusWeeks(8));
        bookingDtoId4.setItem(expectedItem);

        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName("Test");
        expectedUser.setEmail("Testov@Test.test");

        expectedAnotherUser = new User();
        expectedAnotherUser.setId(2L);
        expectedAnotherUser.setName("Test");
        expectedAnotherUser.setEmail("TestUser@User.ru");

        expectedItem = new Item();
        expectedItem.setId(1L);
        expectedItem.setName("Test Item");
        expectedItem.setDescription("1111");
        expectedItem.setUser(expectedUser);
        expectedItem.setAvailable(true);
    }

    @Test
    void shouldAddRequestBooking() throws Exception {
        BookingDto bookingDto1 = BookingMapper
                .mapToBookingDto(BookingMapper
                        .mapToBooking(expectedBookingRequest, expectedItem, expectedUser));

        when(bookingService.addBookingRequest(anyLong(), any()))
                .thenReturn(bookingDto1);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(expectedBookingRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingRequest.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(expectedBookingRequest.getStart().toString())))
                .andExpect(jsonPath("$.end", is(expectedBookingRequest.getEnd().toString())));

    }

    @Test
    void shouldBeConfirmed() throws Exception {
        BookingDto bookingDto1 = BookingMapper
                .mapToBookingDto(BookingMapper
                        .mapToBooking(expectedBookingRequest, expectedItem, expectedUser));
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
                        .mapToBooking(expectedBookingRequest, expectedItem, expectedUser));

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
                        .mapToBooking(expectedBookingRequest, expectedItem, expectedUser));

        bookingDto1.setBooker(expectedAnotherUser);
        bookingDtoId2.setBooker(expectedAnotherUser);
        bookingDtoId3.setBooker(expectedAnotherUser);
        bookingDtoId4.setBooker(expectedAnotherUser);

        when(bookingService.getBookingsByConditions(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDto1, bookingDtoId2, bookingDtoId3, bookingDtoId4));

        mvc.perform(get("/bookings?state=FUTURE")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDto1.getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookingDto1.getEnd().toString())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[1].id", is(bookingDtoId2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].start", is(bookingDtoId2.getStart().toString())))
                .andExpect(jsonPath("$.[1].end", is(bookingDtoId2.getEnd().toString())))
                .andExpect(jsonPath("$.[1].booker.id", is(bookingDtoId2.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[2].id", is(bookingDtoId3.getId()), Long.class))
                .andExpect(jsonPath("$.[2].start", is(bookingDtoId3.getStart().toString())))
                .andExpect(jsonPath("$.[2].end", is(bookingDtoId3.getEnd().toString())))
                .andExpect(jsonPath("$.[2].booker.id", is(bookingDtoId3.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[3].id", is(bookingDtoId4.getId()), Long.class))
                .andExpect(jsonPath("$.[3].start", is(bookingDtoId4.getStart().toString())))
                .andExpect(jsonPath("$.[3].end", is(bookingDtoId4.getEnd().toString())))
                .andExpect(jsonPath("$.[3].booker.id", is(bookingDtoId4.getBooker().getId()), Long.class));
    }

    @Test
    void shouldReturnAllBookingsForOwner() throws Exception {

        bookingDtoId2.setStart(time.minusWeeks(2));
        bookingDtoId2.setEnd(time.minusWeeks(1));

        bookingDtoId3.setStart(time.minusWeeks(10));
        bookingDtoId3.setEnd(time.minusWeeks(9));

        bookingDtoId4.setStart(time.minusWeeks(20));
        bookingDtoId4.setEnd(time.minusWeeks(19));

        bookingDtoId2.setBooker(expectedAnotherUser);
        bookingDtoId3.setBooker(expectedAnotherUser);
        bookingDtoId4.setBooker(expectedAnotherUser);

        when(bookingService.getBookingsForOwner(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDtoId2, bookingDtoId3, bookingDtoId4));

        mvc.perform(get("/bookings/owner?state=PAST")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDtoId2.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDtoId2.getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookingDtoId2.getEnd().toString())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDtoId2.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[1].id", is(bookingDtoId3.getId()), Long.class))
                .andExpect(jsonPath("$.[1].start", is(bookingDtoId3.getStart().toString())))
                .andExpect(jsonPath("$.[1].end", is(bookingDtoId3.getEnd().toString())))
                .andExpect(jsonPath("$.[1].booker.id", is(bookingDtoId3.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[2].id", is(bookingDtoId4.getId()), Long.class))
                .andExpect(jsonPath("$.[2].start", is(bookingDtoId4.getStart().toString())))
                .andExpect(jsonPath("$.[2].end", is(bookingDtoId4.getEnd().toString())))
                .andExpect(jsonPath("$.[2].booker.id", is(bookingDtoId4.getBooker().getId()), Long.class));
    }

}
