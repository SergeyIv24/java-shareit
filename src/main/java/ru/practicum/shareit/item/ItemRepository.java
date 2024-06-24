package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, ItemDto item);

    Optional<Item> getItemById(Long itemId);

    Collection<ItemDto> getMyItems(Long userId);

    Collection<ItemDto> searchByText(String text);
}
