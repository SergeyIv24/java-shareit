package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory {
    private Long itemId = 0L;
    private List<Item> items = new ArrayList<>();

    public ItemDto addItem(Long userId, ItemDto itemDto) {
        Item newItem = ItemMapper.mapToItem(itemDto);
        newItem.setOwnerId(userId);
        newItem.setItemId(defineItemId());
        items.add(newItem);
        return itemDto;
    }



    public Optional<ItemDto> getItemById(Long itemId) {
        return items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .map(ItemMapper::mapToItemDto).findFirst();
    }

    public Collection<ItemDto> getMyItems(Long userId) {
        return items.stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }




    private Long defineItemId() {
        if (items.isEmpty()) {
            itemId = 1L;
            return itemId;
        }
        return ++itemId;
    }
}
