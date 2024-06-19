package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Data
public class User {
    Long id;
    @NotBlank(message = "Empty user name")
    @Size(min = 1, max = 20, message = "Bad user name length")
    String name;
    @NotBlank(message = "Empty email name")
    @Email
    String email;
    Collection<Item> items;
}
