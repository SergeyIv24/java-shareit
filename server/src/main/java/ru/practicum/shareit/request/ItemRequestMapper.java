package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.RequestedItemsDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    @Autowired
    private ItemRepository itemRepository;

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setUser(user);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(itemRequestDto.getCreated());
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setUserId(itemRequest.getUser().getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public ItemRequestResponseDto mapToItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setId(itemRequest.getId());
        itemRequestResponseDto.setUserId(itemRequest.getUser().getId());
        itemRequestResponseDto.setDescription(itemRequest.getDescription());
        itemRequestResponseDto.setCreated(itemRequest.getCreated());
        List<RequestedItemsDto> items = itemRepository.findByRequestId(itemRequest.getId())
                .stream()
                .map(ItemMapper::mapToRequestedItem)
                .toList();
        itemRequestResponseDto.setItems(items);
        return itemRequestResponseDto;
    }
}
