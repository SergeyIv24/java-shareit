package ru.practicum.shareit.booking;

import lombok.Getter;

@Getter
public enum BookingStatus {
    WAITING(1),
    APPROVED(2),
    REJECTED(3);

    private final int status;

    BookingStatus(int status) {
        this.status = status;
    }
}
