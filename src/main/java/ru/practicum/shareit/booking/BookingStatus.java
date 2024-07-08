package ru.practicum.shareit.booking;

import lombok.Getter;

@Getter
enum BookingStatus {
    WAITING(1),
    APPROVED(2),
    REJECTED(3);

    private final int status;

    BookingStatus(int status) {
        this.status = status;
    }
}
