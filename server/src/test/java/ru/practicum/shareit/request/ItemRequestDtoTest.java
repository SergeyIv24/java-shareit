package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestDtoTest {

    private final JacksonTester<ItemRequestDto> json;

    @Test
    void shouldReturnCorrectJSONVersion() throws Exception {
        ItemRequestDto expectedItemRequestDto = new ItemRequestDto();
        expectedItemRequestDto.setId(1L);
        expectedItemRequestDto.setDescription("Test");
        expectedItemRequestDto.setUserId(1L);

        JsonContent<ItemRequestDto> jsonItemDRequestDto = json.write(expectedItemRequestDto);

        assertThat(jsonItemDRequestDto).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonItemDRequestDto).extractingJsonPathValue("$.description")
                .isEqualTo(expectedItemRequestDto.getDescription());
        assertThat(jsonItemDRequestDto).extractingJsonPathNumberValue("$.userId")
                .isEqualTo(1);
    }
}
