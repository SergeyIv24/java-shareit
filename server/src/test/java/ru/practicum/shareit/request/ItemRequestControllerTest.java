package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private static ItemRequestDto request1Correct;
    private static ItemRequestDto notCorrectRequest;
    private static ItemRequestResponseDto requestWithResponses1;
    private static ItemRequestResponseDto requestWithResponses2;
    private static ItemRequestResponseDto requestWithResponses3;
    private static ItemRequestResponseDto requestWithResponses4;
    private static Item item1;
    private static Item item2;

    @BeforeAll
    static void setup() {

        User item1And2Owner = new User();
        item1And2Owner.setId(1L);
        item1And2Owner.setName("Sergey");
        item1And2Owner.setEmail("SomeEmail@Email.com");

        User req1And2Owner = new User();
        req1And2Owner.setId(2L);
        req1And2Owner.setName("Leo");
        req1And2Owner.setEmail("Leo@Email.com");

        item1 = new Item();
        item1.setId(1L);
        item1.setUser(item1And2Owner);
        item1.setName("Hammer");
        item1.setDescription("Hammer");
        item1.setAvailable(true);
        item1.setRequestId(1L);

        item2 = new Item();
        item2.setId(2L);
        item2.setUser(item1And2Owner);
        item2.setName("Another Hammer");
        item2.setDescription("Hammer");
        item2.setAvailable(true);
        item2.setRequestId(1L);

        request1Correct = new ItemRequestDto();
        request1Correct.setId(1L);
        request1Correct.setUserId(1L);
        request1Correct.setDescription("I need some pans");
        request1Correct.setCreated(LocalDateTime.of(2034, 12, 12, 12, 12, 12));
        notCorrectRequest = new ItemRequestDto();

        ItemRequestDto request2Correct = new ItemRequestDto();
        request2Correct.setId(2L);
        request2Correct.setUserId(1L);
        request2Correct.setDescription("I need hammer");
        request2Correct.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));

        requestWithResponses1 = new ItemRequestResponseDto();
        requestWithResponses1.setId(2L);
        requestWithResponses1.setUserId(2L);
        requestWithResponses1.setDescription("Tor`s hammer");
        requestWithResponses1.setCreated(LocalDateTime.of(2034, 12, 12, 12, 12, 12));
        requestWithResponses1.setItems(List.of(ItemMapper.mapToRequestedItem(item1), ItemMapper.mapToRequestedItem(item2)));

        requestWithResponses2 = new ItemRequestResponseDto();
        requestWithResponses2.setId(3L);
        requestWithResponses2.setUserId(2L);
        requestWithResponses2.setDescription("Drill");
        requestWithResponses2.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));
        requestWithResponses2.setItems(List.of());

        requestWithResponses3 = new ItemRequestResponseDto();
        requestWithResponses3.setId(4L);
        requestWithResponses3.setUserId(1L);
        requestWithResponses3.setDescription("Window");
        requestWithResponses3.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));
        requestWithResponses3.setItems(List.of());

        requestWithResponses4 = new ItemRequestResponseDto();
        requestWithResponses4.setId(5L);
        requestWithResponses4.setUserId(1L);
        requestWithResponses4.setDescription("Window");
        requestWithResponses4.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));
        requestWithResponses4.setItems(List.of());
    }

    @Test
    void shouldCreateNewItemRequest() throws Exception {

        when(itemRequestService.addRequest(anyLong(), any(), any()))
                .thenReturn(request1Correct);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(request1Correct))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1Correct.getId()), Long.class))
                .andExpect(jsonPath("$.userId", is(request1Correct.getUserId()), Long.class))
                .andExpect(jsonPath("$.description", is(request1Correct.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(request1Correct.getCreated().toString()),
                        LocalDateTime.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .addRequest(anyLong(), any(), any());
    }

    @Test
    void shouldReturnRequestById() throws Exception {
        ItemRequestResponseDto requestResponse = new ItemRequestResponseDto();
        requestResponse.setDescription(request1Correct.getDescription());
        requestResponse.setCreated(request1Correct.getCreated());
        requestResponse.setItems(List.of());

        when(itemRequestService.getRequestById(anyLong()))
                .thenReturn(requestResponse);

        mvc.perform(get("/requests/1").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(requestResponse.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(requestResponse.getCreated().toString()), LocalDateTime.class))
                .andExpect(jsonPath("$.items", is(requestResponse.getItems()), List.class));

        Mockito.verify(itemRequestService)
                .getRequestById(anyLong());

    }

    @Test
    void shouldNotReturnFailedId() throws Exception {
        when(itemRequestService.getRequestById(anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/requests/100").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //return not found if user does not have requests
    @Test
    void shouldNotReturnListsOfRequestsIfUserDoesNotHaveRequests() throws Exception {

        when(itemRequestService.getMyRequests(anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //return correct response if user has requests
    @Test
    void shouldReturnAllUsersRequests() throws Exception {

        when(itemRequestService.getMyRequests(anyLong()))
                .thenReturn(List.of(requestWithResponses1, requestWithResponses2));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestWithResponses1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestWithResponses1.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(requestWithResponses1.getCreated().toString()), LocalDateTime.class))
                .andExpect(jsonPath("$.[0].items.[0].id", is(requestWithResponses1.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.[0].items.[0].name", is(requestWithResponses1.getItems().get(0).getName()), String.class))
                .andExpect(jsonPath("$.[0].items.[0].ownerId", is(requestWithResponses1.getItems().get(0).getOwnerId()), Long.class))
                .andExpect(jsonPath("$.[1].description", is(requestWithResponses2.getDescription()), String.class))
                .andExpect(jsonPath("$.[1].created", is(requestWithResponses2.getCreated().toString()), LocalDateTime.class))
                .andExpect(jsonPath("$.[1].items", equalTo(List.of())));


    }

    //return not found if user is not existed
    @Test
    void shouldReturnErrorIfUserIsNotExisted() throws Exception {

        when(itemRequestService.getMyRequests(anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 100L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //return all request, except user`s requests.
    @Test
    void shouldGetAllRequest() throws Exception {

        when(itemRequestService.getRequests(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(requestWithResponses3));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestWithResponses3.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestWithResponses3.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(requestWithResponses3.getCreated().toString()), LocalDateTime.class))
                .andExpect(jsonPath("$.[0].items", equalTo(List.of())));
    }
}
