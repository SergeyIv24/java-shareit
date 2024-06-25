package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Item addItem(Long userId, ItemDto itemDto);

    Item updateItem(Long itemId, ItemDto item);

    Optional<Item> getItemById(Long itemId);

    Collection<Item> getMyItems(Long userId);

    Collection<Item> searchByText(String text);
}
