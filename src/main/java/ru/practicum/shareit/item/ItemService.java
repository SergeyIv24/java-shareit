package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long ownerId, ItemDto item);

    Optional<ItemDto> getItemById(Long itemId);

    Collection<ItemDto> getMyItems(Long userId);

    Collection<ItemDto> searchByRequest(String text);


}
