package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.user.User;

@Data
public class Item {
    Long itemId;
    @NotBlank(message = "Bad item name")
    @Size(min = 5, max = 100, message = "Bad name length")
    String name;
    @NotBlank(message = "Bad description")
    @Size(min = 50, max = 200, message = "Bad description length")
    String description;
    @NotNull
    Long ownerId;
    @NotNull
    Boolean available;
}
