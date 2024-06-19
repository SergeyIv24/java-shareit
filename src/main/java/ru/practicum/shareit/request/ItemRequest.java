package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 */

@Data
public class ItemRequest {
    Long requestId;
    Long userId;
    @NotBlank(message = "Empty description")
    @Size(min = 2, max = 300, message = "Bad description length")
    String description;
}
