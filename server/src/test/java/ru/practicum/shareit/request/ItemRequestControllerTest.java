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

    private static ItemRequestDto requestCorrect;
    private static ItemRequestDto notCorrectRequest;
    private static ItemRequestResponseDto requestWithResponsesId2;
    private static ItemRequestResponseDto requestWithResponsesId3;
    private static ItemRequestResponseDto requestWithResponsesId4;
    private static ItemRequestResponseDto requestWithResponsesId5;
    private static Item expectedItemHammer;
    private static Item expectedAnotherItem;

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

        expectedItemHammer = new Item();
        expectedItemHammer.setId(1L);
        expectedItemHammer.setUser(item1And2Owner);
        expectedItemHammer.setName("Hammer");
        expectedItemHammer.setDescription("Hammer");
        expectedItemHammer.setAvailable(true);
        expectedItemHammer.setRequestId(1L);

        expectedAnotherItem = new Item();
        expectedAnotherItem.setId(2L);
        expectedAnotherItem.setUser(item1And2Owner);
        expectedAnotherItem.setName("Another Hammer");
        expectedAnotherItem.setDescription("Hammer");
        expectedAnotherItem.setAvailable(true);
        expectedAnotherItem.setRequestId(1L);

        requestCorrect = new ItemRequestDto();
        requestCorrect.setId(1L);
        requestCorrect.setUserId(1L);
        requestCorrect.setDescription("I need some pans");
        requestCorrect.setCreated(LocalDateTime.of(2034, 12, 12, 12, 12, 12));
        notCorrectRequest = new ItemRequestDto();

        ItemRequestDto request2Correct = new ItemRequestDto();
        request2Correct.setId(2L);
        request2Correct.setUserId(1L);
        request2Correct.setDescription("I need hammer");
        request2Correct.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));

        requestWithResponsesId2 = new ItemRequestResponseDto();
        requestWithResponsesId2.setId(2L);
        requestWithResponsesId2.setUserId(2L);
        requestWithResponsesId2.setDescription("Tor`s hammer");
        requestWithResponsesId2.setCreated(LocalDateTime.of(2034, 12, 12, 12, 12, 12));
        requestWithResponsesId2.setItems(List.of(ItemMapper.mapToRequestedItem(expectedItemHammer),
                ItemMapper.mapToRequestedItem(expectedAnotherItem)));

        requestWithResponsesId3 = new ItemRequestResponseDto();
        requestWithResponsesId3.setId(3L);
        requestWithResponsesId3.setUserId(2L);
        requestWithResponsesId3.setDescription("Drill");
        requestWithResponsesId3.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));
        requestWithResponsesId3.setItems(List.of());

        requestWithResponsesId4 = new ItemRequestResponseDto();
        requestWithResponsesId4.setId(4L);
        requestWithResponsesId4.setUserId(1L);
        requestWithResponsesId4.setDescription("Window");
        requestWithResponsesId4.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));
        requestWithResponsesId4.setItems(List.of());

        requestWithResponsesId5 = new ItemRequestResponseDto();
        requestWithResponsesId5.setId(5L);
        requestWithResponsesId5.setUserId(1L);
        requestWithResponsesId5.setDescription("Window");
        requestWithResponsesId5.setCreated(LocalDateTime.of(2034, 12, 12, 14, 14, 14));
        requestWithResponsesId5.setItems(List.of());
    }

    @Test
    void shouldCreateNewItemRequest() throws Exception {

        when(itemRequestService.addRequest(anyLong(), any(), any()))
                .thenReturn(requestCorrect);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestCorrect))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestCorrect.getId()), Long.class))
                .andExpect(jsonPath("$.userId", is(requestCorrect.getUserId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestCorrect.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(requestCorrect.getCreated().toString()),
                        LocalDateTime.class));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .addRequest(anyLong(), any(), any());
    }

    @Test
    void shouldReturnRequestById() throws Exception {
        ItemRequestResponseDto requestResponse = new ItemRequestResponseDto();
        requestResponse.setDescription(requestCorrect.getDescription());
        requestResponse.setCreated(requestCorrect.getCreated());
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
                .thenReturn(List.of(requestWithResponsesId2, requestWithResponsesId3));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestWithResponsesId2.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestWithResponsesId2.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(requestWithResponsesId2.getCreated().toString()), LocalDateTime.class))
                .andExpect(jsonPath("$.[0].items.[0].id", is(requestWithResponsesId2.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.[0].items.[0].name", is(requestWithResponsesId2.getItems().get(0).getName()), String.class))
                .andExpect(jsonPath("$.[0].items.[0].ownerId", is(requestWithResponsesId2.getItems().get(0).getOwnerId()), Long.class))
                .andExpect(jsonPath("$.[1].description", is(requestWithResponsesId3.getDescription()), String.class))
                .andExpect(jsonPath("$.[1].created", is(requestWithResponsesId3.getCreated().toString()), LocalDateTime.class))
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
                .thenReturn(List.of(requestWithResponsesId4));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestWithResponsesId4.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestWithResponsesId4.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(requestWithResponsesId4.getCreated().toString()), LocalDateTime.class))
                .andExpect(jsonPath("$.[0].items", equalTo(List.of())));
    }
}
