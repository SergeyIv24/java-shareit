package ru.practicum.shareit.item;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private static ItemDto item1;
    private static ItemDto itemAsResponseToRequest;

    @BeforeAll
    static void initializeSomeItems() {
        item1 = new ItemDto();
        item1.setId(1L);
        item1.setName("Пакет для мусора");
        item1.setDescription("В пакете можно вынести мусор");
        item1.setAvailable(true);

        itemAsResponseToRequest = new ItemDto();
        itemAsResponseToRequest.setId(2L);
        itemAsResponseToRequest.setName("Пакет");
        itemAsResponseToRequest.setDescription("Для других пакетов");
        itemAsResponseToRequest.setAvailable(true);
        itemAsResponseToRequest.setRequestId(1L);
    }

    @Test
    void shouldCreateItem() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(item1);

        mvc.perform(post("/items")
                .content(mapper.writeValueAsString(item1))
                .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName()), String.class))
                .andExpect(jsonPath("$.description", is(item1.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(item1.getAvailable()), Boolean.class));
    }

    @Test
    void shouldCreateItemAsResponseToRequest() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemAsResponseToRequest);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemAsResponseToRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemAsResponseToRequest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemAsResponseToRequest.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemAsResponseToRequest.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemAsResponseToRequest.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(itemAsResponseToRequest.getRequestId()), Long.class));
    }




}
