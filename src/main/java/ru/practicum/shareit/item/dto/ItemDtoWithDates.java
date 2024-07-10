package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class ItemDtoWithDates {
    private Long id;
    @NotBlank
    @Size(max = 100, message = "Bad name length")
    private String name;
    @NotBlank
    @Size(max = 200, message = "Bad description length")
    private String description;
    @NotNull
    private Boolean available;
    private LocalDateTime start;
    private LocalDateTime end;
}
