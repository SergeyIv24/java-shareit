package ru.practicum.shareit.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserServiceTest {

    private final EntityManager em;
    private final UserService userService;

    private static User user;

    @BeforeAll
    static void setup() {
        user = new User();
        user.setName("Test");
        user.setEmail("Testov@test.test");
    }

    @Test
    void shouldGetUserAfterCreatingAndUpdating() {

        UserDto savedUser = userService.createUser(UserMapper.mapToUserDto(user));

        assertThat(savedUser.getId(), notNullValue());
        assertThat(savedUser.getName(), equalTo("Test"));
        assertThat(savedUser.getEmail(), equalTo("Testov@test.test"));

        savedUser.setName("Mat");
        savedUser.setEmail("Mat@mail.com");

        UserDto updateUser = userService.updateUser(savedUser.getId(), savedUser);

        assertThat(updateUser.getId(), equalTo(savedUser.getId()));
        assertThat(updateUser.getName(), equalTo("Mat"));
        assertThat(updateUser.getEmail(), equalTo("Mat@mail.com"));

        UserDto gotUser = userService.getUserById(updateUser.getId());

        assertThat(gotUser.getId(), equalTo(updateUser.getId()));
        assertThat(gotUser.getName(), equalTo(updateUser.getName()));
        assertThat(gotUser.getEmail(), equalTo(updateUser.getEmail()));
    }
}
