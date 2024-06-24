package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private Long itemId = 0L;
    private final List<Item> items = new ArrayList<>();

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        Item newItem = ItemMapper.mapToItem(itemDto);
        newItem.setOwnerId(userId);
        newItem.setId(defineItemId());
        items.add(newItem);
        return ItemMapper.mapToItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto item) {
        Item updatingItem = getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        if (item.getName() != null) {
            updatingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatingItem.setAvailable(item.getAvailable());
        }
        deleteItem(itemId);
        items.add(updatingItem);
        return ItemMapper.mapToItemDto(updatingItem);
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
    }

    @Override
    public Collection<ItemDto> getMyItems(Long userId) {
        return items.stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchByText(String text) {
        return items.stream()
                .filter(item -> (item.getName().toLowerCase().contains(text))
                        || (item.getDescription().toLowerCase().contains(text))
                        && item.getAvailable())
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    public void deleteItem(Long itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
    }

    private Long defineItemId() {
        if (items.isEmpty()) {
            itemId = 1L;
            return itemId;
        }
        return ++itemId;
    }
}
