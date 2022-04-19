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

    //проверка названия фильма или имя пользователя
    public static void checkName(Object obj) throws ValidationException {
        if (obj instanceof Film) {
            if (((Film) obj).getFilmName().isBlank() || ((Film) obj).getFilmName() == null) {
                log.warn("Не указано название фильма");
                throw new ValidationException("Название фильма не может быть пустым");
            }
        }
        if (obj instanceof User) {
            if (((User) obj).getLogin().isBlank() || ((User) obj).getLogin() == null || ((User) obj).getLogin()
                                                                                                    .contains(" ")) {
                log.warn("Не указан логин пользователя");
                throw new ValidationException("Логин пользователя не может быть пустым");
            }
        }
    }


    @Override
    public String getMessage() {
        return messages;
    }


}
