package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto, LocalDateTime now);

    List<ItemRequestResponseDto> getMyRequests(long userId);

    List<ItemRequestResponseDto> getRequests(int from, int size, long userId);

    ItemRequestResponseDto getRequestById(long requestId);

}
