package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
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
        User itemOwner = userRepository.findById(userId).orElseThrow();
        Item addingItem = ItemMapper.mapToItem(itemDto);
        addingItem.setUser(itemOwner);
        return ItemMapper.mapToItemDto(itemRepository.save(addingItem));
    }

    @Override
    public ItemDto updateItem(Long itemId, Long ownerId, ItemDto item) {
        checkUser(ownerId);
        checkUsersItems(itemId, ownerId);

        Item updatingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        if (item.getName() != null) {
            updatingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatingItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.mapToItemDto(itemRepository.save(updatingItem));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper
                .mapToItemDto(itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException("Item is not found")));
    }

    @Override
    public Collection<ItemDtoWithDates> getMyItems(Long userId) {
        return itemRepository.findByUserId(userId)
                .stream()
                .map(ItemMapper::mapToItemDtoWithDates)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchByRequest(String text) {
       if (text.isEmpty()) {
            return List.of();
        }

        List<Item> searchedItems = itemRepository.findBySearch(text);

        if (searchedItems.isEmpty()) {
            throw new NotFoundException("Item is not found");
        }
        return searchedItems.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());

    }

    private void checkUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user is not found"));
    }

    private void checkUsersItems(Long itemId, Long ownerId) {
        Item existedItem = itemRepository.findById(itemId)
                .orElseThrow();
        if (!existedItem.getUser().getId().equals(ownerId)) {
            throw new NotFoundException("Item is not found");
        }
    }
}
