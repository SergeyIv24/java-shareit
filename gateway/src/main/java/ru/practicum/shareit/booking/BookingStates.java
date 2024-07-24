package ru.practicum.shareit.booking;

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
