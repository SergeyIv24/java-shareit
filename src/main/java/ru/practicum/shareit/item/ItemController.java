package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Item addItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemServiceImpl.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updateItem(@PathVariable(value = "itemId") Long itemId,
                           @RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                           @Valid @RequestBody Item item) {
        return itemServiceImpl.updateItem(itemId, ownerId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item getItemById(@PathVariable(value = "itemId") Long itemId) {
        return itemServiceImpl.getItemById(itemId).orElseThrow();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Item> getMyItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.getMyItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Item> searchByNameOrDesc(@RequestParam("text") String text) {
        return itemServiceImpl.searchByRequest(text);
    }

}
