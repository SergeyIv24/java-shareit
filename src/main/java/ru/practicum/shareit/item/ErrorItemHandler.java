package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

//Return correct http code
@RestControllerAdvice
public class ErrorItemHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerValidationException(final ValidationException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerNotFoundException(final NotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlerConflictException(final ConflictException e) {
        return e.getMessage();
    }
}
