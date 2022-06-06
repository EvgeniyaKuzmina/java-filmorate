package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundUser(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundFilm(FilmNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<FieldError> errors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError error : errors) {
            sb.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(", ");
        }
        return new ErrorResponse(sb.toString());
    }

    @ExceptionHandler(RatingNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(RatingNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(GenreNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }


}
