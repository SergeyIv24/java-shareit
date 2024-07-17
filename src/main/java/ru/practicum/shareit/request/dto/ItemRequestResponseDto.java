package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.RequestedItemsDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestResponseDto {

    private String description;
    private LocalDateTime created;
    private List<RequestedItemsDto> responses;


}
