package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Item addItem(Long userId, ItemDto itemDto);

    Item updateItem(Long itemId, Long ownerId, Item item);

    Optional<Item> getItemById(Long itemId);

    Collection<Item> getMyItems(Long userId);

    Collection<Item> searchByRequest(String text);


}
