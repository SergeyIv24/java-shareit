package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepositoryInMemory itemRepository;
    private final UserRepository userRepository;

    public ItemDto addItem(Long userId, ItemDto itemDto) {
        checkUser(userId);
        return itemRepository.addItem(userId, itemDto);
    }

    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItemById(itemId).orElseThrow();
    }

    public Collection<ItemDto> getMyItems(Long userId) {
        return itemRepository.getMyItems(userId);
    }

    private void checkUser(Long userId) {
        userRepository.getUserById(userId).orElseThrow();
    }
}
