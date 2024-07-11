package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;

import java.time.Instant;
import java.util.Collection;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long ownerId, ItemDto item);

    ItemDtoWithDates getItemById(Long itemId, Instant now, Long ownerId);

    Collection<ItemDtoWithDates> getMyItems(Long userId, Instant now);

    Collection<ItemDto> searchByRequest(String text);

    CommentsDto addComment(Long userId, CommentsDto commentsDto, Long itemId);
}
