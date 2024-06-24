package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Item {
    Long id;
    @NotBlank
    @Size(max = 100, message = "Bad name length")
    String name;
    @NotBlank
    @Size(max = 200, message = "Bad description length")
    String description;
    Long ownerId;
    @NotNull
    Boolean available;
}
