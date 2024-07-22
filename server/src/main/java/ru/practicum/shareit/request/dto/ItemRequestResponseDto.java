package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.RequestedItemsDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Long userId;
    private List<RequestedItemsDto> responses;


}
