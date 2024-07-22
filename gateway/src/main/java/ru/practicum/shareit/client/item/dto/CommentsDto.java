package ru.practicum.shareit.client.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsDto {
    //private Long id;
    @NotBlank
    private String text;
    private String authorName;
    //private LocalDateTime created;
}
