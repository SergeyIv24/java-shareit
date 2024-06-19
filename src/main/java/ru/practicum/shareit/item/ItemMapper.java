package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public final class ItemMapper{
    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        //itemDto.setItemId(item.getItemId());
        itemDto.setName(item.getName());
        itemDto.setDescription(itemDto.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static Item mapToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }
}
