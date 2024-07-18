package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    @NotBlank
    @Size(max = 100, message = "Bad name length")
    private String name;
    @NotBlank
    @Size(max = 200, message = "Bad description length")
    private String description;
    @NotNull
    private Boolean available;

    private Long requestId;
}
