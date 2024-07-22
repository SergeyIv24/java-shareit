package ru.practicum.shareit.client.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentsDto {
    @NotBlank
    private String text;
}
