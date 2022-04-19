package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class ValidationException extends Exception {

    private final String messages;

    public ValidationException(String messages) {
        super(messages);
        this.messages = messages;
    }


    @Override
    public String getMessage() {
        return messages;
    }


}
