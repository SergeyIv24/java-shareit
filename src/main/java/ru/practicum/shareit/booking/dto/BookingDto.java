package ru.practicum.shareit.booking.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingDto {
    private Long bookingId;
    private Long itemId;
    private Long userId;
    private LocalDate start;
    private LocalDate end;
}
