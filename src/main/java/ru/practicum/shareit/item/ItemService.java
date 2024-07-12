package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long itemId, long ownerId, ItemDto item);

    ItemDtoWithDates getItemById(long itemId, LocalDateTime now, long ownerId);

    Collection<ItemDtoWithDates> getMyItems(long userId, LocalDateTime now);

    Collection<ItemDto> searchByRequest(String text);

    CommentsDto addComment(long userId, CommentsDto commentsDto, long itemId);
}
