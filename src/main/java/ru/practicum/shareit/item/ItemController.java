package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto addItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemServiceImpl.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@PathVariable(value = "itemId") Long itemId,
                           @RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                           @RequestBody ItemDto item) {
        return itemServiceImpl.updateItem(itemId, ownerId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable(value = "itemId") Long itemId) {
        return itemServiceImpl.getItemById(itemId).orElseThrow();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDtoWithDates> getMyItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.getMyItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchByNameOrDesc(@RequestParam("text") String text) {
        return itemServiceImpl.searchByRequest(text);
    }

}
