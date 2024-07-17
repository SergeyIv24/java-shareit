package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class RequestedItemsDto {
    private Long id;
    private String name;
    private Long ownerId;
}
