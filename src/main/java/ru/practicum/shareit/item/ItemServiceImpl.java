package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        checkUser(userId);
        return ItemMapper.mapToItemDto(itemRepository.addItem(userId, itemDto));
    }

    @Override
    public ItemDto updateItem(Long itemId, Long ownerId, ItemDto item) {
        checkUser(ownerId);
        checkUsersItems(itemId, ownerId);
        return ItemMapper.mapToItemDto(itemRepository.updateItem(itemId, item));
    }

    @Override
    public Optional<ItemDto> getItemById(Long itemId) {
        return itemRepository
                .getItemById(itemId)
                .map(ItemMapper::mapToItemDto);
    }

    @Override
    public Collection<ItemDto> getMyItems(Long userId) {
        return itemRepository.getMyItems(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchByRequest(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        Collection<Item> searchedItems = itemRepository.searchByText(text.toLowerCase());
        if (searchedItems.isEmpty()) {
            throw new NotFoundException("Item is not found");
        }
        return searchedItems.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void checkUser(Long userId) {
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("user is not found"));
    }

    private void checkUsersItems(Long itemId, Long ownerId) {
        Item existedItem = itemRepository.getItemById(itemId)
                .orElseThrow();
        if (!existedItem.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Item is not found");
        }
    }
}
