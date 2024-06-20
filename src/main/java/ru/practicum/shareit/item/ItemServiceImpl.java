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

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addItem(Long userId, ItemDto itemDto) {
        checkUser(userId);
        return itemRepository.addItem(userId, itemDto);
    }

    @Override
    public Item updateItem(Long itemId, Long ownerId, Item item) {
        checkUser(ownerId);
        checkUsersItems(itemId, ownerId);
        return itemRepository.updateItem(itemId, item);
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Collection<Item> getMyItems(Long userId) {
        return itemRepository.getMyItems(userId);
    }

    @Override
    public Collection<Item> searchByRequest(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        Collection<Item> searchedItems = itemRepository.searchByText(text.toLowerCase());
        if (searchedItems.isEmpty()) {
            throw new NotFoundException("Item is not found");
        }
        return searchedItems;
    }

    private void checkUser(Long userId) {
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("user is not found"));
    }

    private void checkUsersItems(Long itemId, Long ownerId) {
        Item existedItem = getItemById(itemId).orElseThrow();
        if (!existedItem.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Item is not found");
        }
    }
}
