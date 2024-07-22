package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookingWithInfoDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDtoWithDates extends ItemDto {
    private Long id;
    @NotBlank
    @Size(max = 100, message = "Bad name length")
    private String name;
    @NotBlank
    @Size(max = 200, message = "Bad description length")
    private String description;
    @NotNull
    private Boolean available;
    private BookingWithInfoDto lastBooking;
    private BookingWithInfoDto nextBooking;
    private List<CommentsDto> comments;
}
