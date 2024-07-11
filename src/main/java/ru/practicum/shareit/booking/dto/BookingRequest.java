package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


@Data
public class BookingRequest {
    private Long id;
    @NotNull
    private Long itemId;
    private User booker; //Who takes item
    @NotNull
    @FutureOrPresent
    @JsonFormat(timezone = "Greenwich Mean Time")
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent
    private LocalDateTime end;
    private String status;
}
