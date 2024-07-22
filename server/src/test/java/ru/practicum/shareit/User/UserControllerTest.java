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

    private static UserDto userDto1;
    private static UserDto userDto2;

    @BeforeAll
    static void createUsers() {
        userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setName("Test1");
        userDto1.setEmail("Test1@test.com");

        userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setName("Test2");
        userDto2.setEmail("Test2@test.com");
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.createUser(any())).thenReturn(userDto1);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));

    }

    @Test
    void shouldUpdateUser() throws Exception  {
        userDto2.setName("Another Name");
        userDto2.setEmail("Another@Email.cooom");

        when(userService.updateUser(anyLong(), any()))
                .thenReturn(userDto1);

        mvc.perform(patch("/users/1")
                .content(mapper.writeValueAsString(userDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));

    }

    @Test
    void shouldReturnUserById() throws Exception  {
        when(userService.getUserById(anyLong()))
                .thenReturn(userDto1);

        mvc.perform(get("/users/1").characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));
    }

    @Test
    void shouldReturnAllUsers() throws Exception  {

        when(userService.getAllUsers())
                .thenReturn(List.of(userDto1, userDto2));

        mvc.perform(get("/users").characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.[0].email", is(userDto1.getEmail()), String.class))
                .andExpect(jsonPath("$.[1].id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(userDto2.getName()), String.class))
                .andExpect(jsonPath("$.[1].email", is(userDto2.getEmail()), String.class));


    }

    @Test
    void shouldDeleteUser() throws Exception  {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
