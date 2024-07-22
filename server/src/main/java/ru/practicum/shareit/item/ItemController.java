package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto addItem(@RequestHeader(value = "X-Sharer-User-Id") long userId, //@Valid
                           @RequestBody ItemDto itemDto) {
        return itemServiceImpl.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@PathVariable(value = "itemId") long itemId,
                              @RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                              @RequestBody ItemDto item) {
        return itemServiceImpl.updateItem(itemId, ownerId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoWithDates getItemById(@PathVariable(value = "itemId") long itemId,
                                        @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return itemServiceImpl.getItemById(itemId, LocalDateTime.now(), ownerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDtoWithDates> getMyItems(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemServiceImpl.getMyItems(userId, LocalDateTime.now());
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchByNameOrDesc(@RequestParam("text") String text) {
        return itemServiceImpl.searchByRequest(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentsDto addCommentToItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                        @Valid @RequestBody CommentsDto commentsDto,
                                        @PathVariable(value = "itemId") long itemId) {
        return itemServiceImpl.addComment(userId, commentsDto, itemId);
    }
}
