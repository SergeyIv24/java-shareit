package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemDto {
    @NotBlank(message = "Bad item name")
    @Size(max = 100, message = "Bad name length")
    private String name;
    @NotBlank(message = "Bad description")
    @Size(max = 200, message = "Bad description length")
    private String description;
    @NotNull
    private Boolean available;
}
