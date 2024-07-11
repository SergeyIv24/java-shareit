package ru.practicum.shareit.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedException;
import ru.practicum.shareit.exceptions.ValidationException;

//Return correct http code
@RestControllerAdvice
public class ErrorItemHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handlerUnsupportedException(final UnsupportedException e) {
        return new ErrorMessage("Unknown state: UNSUPPORTED_STATUS");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handlerValidationException(final ValidationException e) {
        return new ErrorMessage("Bad input data");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handlerNotFoundException(final NotFoundException e) {
        return new ErrorMessage("Not found");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handlerConflictException(final ConflictException e) {
        return new ErrorMessage("Conflict");
    }

    //Handler returns code 404 after checking field by annotation @Valid. It is needed by Postman tests
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ErrorMessage("Bad input data");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handlerException(final Throwable e) {
        return new ErrorMessage("Smth went wrong");
    }
}
