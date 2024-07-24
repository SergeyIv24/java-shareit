package ru.practicum.shareit.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private static UserDto expectedUserDto;
    private static UserDto expectedAnotherUserDto;

    @BeforeAll
    static void setUp() {
        expectedUserDto = new UserDto();
        expectedUserDto.setId(1L);
        expectedUserDto.setName("Test1");
        expectedUserDto.setEmail("Test1@test.com");

        expectedAnotherUserDto = new UserDto();
        expectedAnotherUserDto.setId(2L);
        expectedAnotherUserDto.setName("Test2");
        expectedAnotherUserDto.setEmail("Test2@test.com");
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.createUser(any())).thenReturn(expectedUserDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(expectedUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail()), String.class));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        expectedAnotherUserDto.setName("Another Name");
        expectedAnotherUserDto.setEmail("Another@Email.cooom");

        when(userService.updateUser(anyLong(), any()))
                .thenReturn(expectedUserDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(expectedUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail()), String.class));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenReturn(expectedUserDto);

        mvc.perform(get("/users/1").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail()), String.class));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {

        when(userService.getAllUsers())
                .thenReturn(List.of(expectedUserDto, expectedAnotherUserDto));

        mvc.perform(get("/users").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(expectedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(expectedUserDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].email", is(expectedUserDto.getEmail()), String.class))
                .andExpect(jsonPath("$.[1].id", is(expectedAnotherUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(expectedAnotherUserDto.getName()), String.class))
                .andExpect(jsonPath("$.[1].email", is(expectedAnotherUserDto.getEmail()), String.class));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
