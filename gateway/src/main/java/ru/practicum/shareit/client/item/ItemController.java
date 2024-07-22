package ru.practicum.shareit.client.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.item.dto.CommentsDto;
import ru.practicum.shareit.client.item.dto.ItemDto;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@PathVariable(value = "itemId") long itemId,
                                             @RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                                             @RequestBody ItemDto item) {
        return itemClient.updateItem(itemId, ownerId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@PathVariable(value = "itemId") long itemId,
                                              @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getMyItems(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemClient.getMyItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchByNameOrDesc(@RequestParam("text") String text) {
        return itemClient.searchByRequest(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addCommentToItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                   @Valid @RequestBody CommentsDto commentsDto,
                                                   @PathVariable(value = "itemId") long itemId) {
        return itemClient.addComment(userId, commentsDto, itemId);
    }
}
