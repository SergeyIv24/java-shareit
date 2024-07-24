package ru.practicum.shareit.Errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorMessage {
    private final String errorMessageFromException;
    private final String error;
}
