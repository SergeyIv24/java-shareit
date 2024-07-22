package ru.practicum.shareit.client.Errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorMessage {
    private final String error;
}
