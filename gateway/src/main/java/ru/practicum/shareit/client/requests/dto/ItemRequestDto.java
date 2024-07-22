package ru.practicum.shareit.client.requests.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemRequestDto {
    @NotBlank
    private String description;
}
