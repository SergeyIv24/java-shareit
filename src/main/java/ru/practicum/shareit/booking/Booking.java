package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;


@Data
public class Booking {
    Long bookingId;
    @NotNull(message = "itemId must exist")
    Long itemId;
    @NotNull(message = "userId must exist")
    Long userId; //Who takes item
    LocalDate start;
    Duration rentDuration;
    LocalDate end;
    Booking isConfirmed;
}
