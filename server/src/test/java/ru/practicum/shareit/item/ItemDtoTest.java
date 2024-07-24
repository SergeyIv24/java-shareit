package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoTest {

    private final JacksonTester<ItemDto> json;

    @Test
    void shouldReturnCorrectJSONVersion() throws Exception {
        ItemDto expectedItemDto = new ItemDto();
        expectedItemDto.setId(1L);
        expectedItemDto.setName("Test");
        expectedItemDto.setDescription("Test");
        expectedItemDto.setAvailable(true);

        JsonContent<ItemDto> jsonItemDto = json.write(expectedItemDto);

        assertThat(jsonItemDto).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonItemDto).extractingJsonPathValue("$.name").isEqualTo(expectedItemDto.getName());
        assertThat(jsonItemDto).extractingJsonPathValue("$.description")
                .isEqualTo(expectedItemDto.getDescription());
        assertThat(jsonItemDto).extractingJsonPathValue("$.available")
                .isEqualTo(expectedItemDto.getAvailable());
    }
}
