package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoTest {

    private final JacksonTester<BookingDto> json;

    @Test
    void shouldReturnCorrectJSONVersion() throws Exception {
        LocalDateTime dataTime = LocalDateTime
                .of(2024, 12, 12, 12, 12, 12, 12);
        User userForBookingDto = new User();
        userForBookingDto.setId(1L);
        userForBookingDto.setName("Test");
        userForBookingDto.setEmail("Testov@em.com");

        Item itemForBookingDto = new Item();
        itemForBookingDto.setId(1L);
        itemForBookingDto.setName("Test item");
        itemForBookingDto.setUser(userForBookingDto);
        itemForBookingDto.setDescription("TestItem");
        itemForBookingDto.setAvailable(true);

        BookingDto expectedBookingDto = new BookingDto();
        expectedBookingDto.setId(1L);
        expectedBookingDto.setItem(itemForBookingDto);
        expectedBookingDto.setBooker(userForBookingDto);
        expectedBookingDto.setStart(dataTime.plusWeeks(1));
        expectedBookingDto.setEnd(dataTime.plusWeeks(2));
        expectedBookingDto.setStatus("Approved");

        JsonContent<BookingDto> jsonBookingDto = json.write(expectedBookingDto);

        assertThat(jsonBookingDto).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonBookingDto).extractingJsonPathValue("$.item.id").isEqualTo(1);
        assertThat(jsonBookingDto).extractingJsonPathValue("$.item.name")
                .isEqualTo(expectedBookingDto.getItem().getName());
        assertThat(jsonBookingDto).extractingJsonPathValue("$.item.description")
                .isEqualTo(expectedBookingDto.getItem().getDescription());
        assertThat(jsonBookingDto).extractingJsonPathValue("$.item.available")
                .isEqualTo(expectedBookingDto.getItem().getAvailable());
        assertThat(jsonBookingDto).extractingJsonPathValue("$.booker.id")
                .isEqualTo(1);
        assertThat(jsonBookingDto).extractingJsonPathValue("$.booker.name")
                .isEqualTo(expectedBookingDto.getBooker().getName());
        assertThat(jsonBookingDto).extractingJsonPathValue("$.start")
                .isEqualTo(expectedBookingDto.getStart().toString());
        assertThat(jsonBookingDto).extractingJsonPathValue("$.end")
                .isEqualTo(expectedBookingDto.getEnd().toString());
    }
}
