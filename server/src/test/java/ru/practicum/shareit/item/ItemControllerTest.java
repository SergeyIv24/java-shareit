package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private static ItemDto expecteditemDto;
    private static ItemDto itemAsResponseToRequestDto;

    private static CommentsDto commentsDto;
    private static final LocalDateTime time = LocalDateTime
            .of(2024, 7, 22, 17, 12, 12, 12);

    @BeforeAll
    static void setup() {
        expecteditemDto = new ItemDto();
        expecteditemDto.setId(1L);
        expecteditemDto.setName("Пакет для мусора");
        expecteditemDto.setDescription("Один пакет");
        expecteditemDto.setAvailable(true);

        itemAsResponseToRequestDto = new ItemDto();
        itemAsResponseToRequestDto.setId(2L);
        itemAsResponseToRequestDto.setName("Пакет");
        itemAsResponseToRequestDto.setDescription("Для других пакетов");
        itemAsResponseToRequestDto.setAvailable(true);
        itemAsResponseToRequestDto.setRequestId(1L);

        commentsDto = new CommentsDto();
        commentsDto.setId(1L);
        commentsDto.setText("Some text");
        commentsDto.setAuthorName("Some name");
        commentsDto.setCreated(time);
    }

    @Test
    void shouldCreateItem() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(expecteditemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(expecteditemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expecteditemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expecteditemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(expecteditemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(expecteditemDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldCreateItemAsResponseToRequest() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemAsResponseToRequestDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemAsResponseToRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemAsResponseToRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemAsResponseToRequestDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemAsResponseToRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemAsResponseToRequestDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(itemAsResponseToRequestDto.getRequestId()), Long.class));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        expecteditemDto.setName("another item");
        expecteditemDto.setDescription("it was updated");
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(expecteditemDto);

        mvc.perform(patch("/items/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(expecteditemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expecteditemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expecteditemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(expecteditemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(expecteditemDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldReturnItemById() throws Exception {
        Item item1 = ItemMapper.mapToItem(expecteditemDto);
        item1.setId(expecteditemDto.getId());
        ItemDtoWithDates item1WithDates = ItemMapper
                .mapToItemDtoWithDates(item1, null, null, null);

        when(itemService.getItemById(anyLong(), any(), anyLong()))
                .thenReturn(item1WithDates);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1WithDates.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1WithDates.getName()), String.class))
                .andExpect(jsonPath("$.description", is(item1WithDates.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(item1WithDates.getAvailable()), Boolean.class));
    }

    @Test
    void shouldReturnUsersItems() throws Exception {
        Item item1 = ItemMapper.mapToItem(expecteditemDto);
        item1.setId(expecteditemDto.getId());
        Item itemAsResponseToRequest = ItemMapper.mapToItem(itemAsResponseToRequestDto);
        itemAsResponseToRequest.setId(itemAsResponseToRequestDto.getId());
        ItemDtoWithDates item1Dates =
                ItemMapper.mapToItemDtoWithDates(item1, null, null, List.of(commentsDto, commentsDto));

        ItemDtoWithDates itemAsResponseDates =
                ItemMapper.mapToItemDtoWithDates(itemAsResponseToRequest, null, null, List.of(commentsDto));

        when(itemService.getMyItems(anyLong(), any()))
                .thenReturn(List.of(item1Dates, itemAsResponseDates));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(item1Dates.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item1Dates.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(item1Dates.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].available", is(item1Dates.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.[0].comments.[0].id",
                        is(item1Dates.getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.[0].comments.[0].text",
                        is(item1Dates.getComments().get(0).getText()), String.class))
                .andExpect(jsonPath("$.[0].comments.[1].id",
                        is(item1Dates.getComments().get(1).getId()), Long.class))
                .andExpect(jsonPath("$.[0].comments.[1].text",
                        is(item1Dates.getComments().get(1).getText()), String.class))
                .andExpect(jsonPath("$.[1].id", is(itemAsResponseDates.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(itemAsResponseDates.getName()), String.class))
                .andExpect(jsonPath("$.[1].description",
                        is(itemAsResponseDates.getDescription()), String.class))
                .andExpect(jsonPath("$.[1].available",
                        is(itemAsResponseDates.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.[1].comments.[0].id",
                        is(itemAsResponseDates.getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.[1].comments.[0].text",
                        is(itemAsResponseDates.getComments().get(0).getText()), String.class));


    }

    @Test
    void shouldSearch() throws Exception {

        when(itemService.searchByRequest(anyString()))
                .thenReturn(List.of(expecteditemDto, itemAsResponseToRequestDto));

        mvc.perform(get("/items/search")
                        .param("text", "Пакет")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(expecteditemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(expecteditemDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(expecteditemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].available", is(expecteditemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.[1].id", is(itemAsResponseToRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(itemAsResponseToRequestDto.getName()), String.class))
                .andExpect(jsonPath("$.[1].description", is(itemAsResponseToRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.[1].available", is(itemAsResponseToRequestDto.getAvailable()), Boolean.class));
    }

    @Test
    void shouldAddCommentToItem() throws Exception {

        when(itemService.addComment(anyLong(), any(), anyLong()))
                .thenReturn(commentsDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentsDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentsDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentsDto.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentsDto.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created", is(commentsDto.getCreated().toString()), String.class));
    }
}
