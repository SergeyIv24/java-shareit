package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldAddRequestBooking() {

    }

    @Test
    void shouldBeConfirmed() {

    }

    @Test
    void shouldReturnBookingById() {

    }

    @Test
    void shouldReturnAllBookingsForUser() {

    }

    @Test
    void shouldReturnAllBookingsForOwner() {

    }

}
