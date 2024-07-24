package ru.practicum.shareit.User;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoTest {

    private final JacksonTester<UserDto> json;

    @Test
    void shouldReturnCorrectJSONVersion() throws Exception {

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(1L);
        expectedUserDto.setName("Test");
        expectedUserDto.setEmail("Testovich@e.com");

        JsonContent<UserDto> result = json.write(expectedUserDto);



    }

}
