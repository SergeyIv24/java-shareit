package ru.practicum.shareit.client.booking;

import lombok.Getter;

@Getter
public enum BookingStates {
    ALL,
    FUTURE,
    PAST,
    CURRENT,
    WAITING,
    REJECTED
}
