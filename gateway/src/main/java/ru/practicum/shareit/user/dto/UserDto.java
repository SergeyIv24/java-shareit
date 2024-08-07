package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Empty user name")
    @Size(min = 1, max = 20, message = "Bad user name length")
    private String name;
    @NotBlank(message = "Empty email name")
    @Email(message = "Is not correct email pattern")
    private String email;
}
