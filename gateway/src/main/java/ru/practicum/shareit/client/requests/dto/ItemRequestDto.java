package ru.practicum.shareit.client.requests.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    //private Long id;
    //private Long userId;
    @NotBlank
    private String description;
    //private LocalDateTime created;
}
