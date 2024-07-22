package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private Long id;
    private Long userId;
    @NotBlank
    private String description;
    private LocalDateTime created;
}
