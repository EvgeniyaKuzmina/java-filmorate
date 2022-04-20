package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

/*
Добрый день.
Я не нашла на гитхабе в пулреквесте какие-либо комментарии по коду, может я не туда смотрела конечно, потому что впервые работаю с ним.
Но исходя из вашего общего комментария (как я его поняла :)) на странице ревью в практикуме, я переделала классы ValidationExceptionFilm и ValidationExceptionUser
Теперь это не классы наследники исключения ValidationException, а самостоятельные классы отвечающие за валидацию вводимых данных.
При этом я убрала некоторые методы ручной валидации, но добавила аннотации, которые успела изучить вчера.
Как на ваш взгляд, оставить как было ранее с ручной проверкой или оставить аннотации по коду?
* */

@Slf4j
public class ValidationFilm {

    private static final LocalDate EARLIEST_DATA_OF_RELEASE = LocalDate.of(1895, 12, 28);

    // проверка даты релиза
    public static void checkDataOfRelease(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(EARLIEST_DATA_OF_RELEASE)) {
            log.warn("Дата релиза указана ранее {}", EARLIEST_DATA_OF_RELEASE);
            throw new ValidationException("Вы не можете указать дату релиза ранее " + EARLIEST_DATA_OF_RELEASE);
        }
    }

    // проверка продолжительности фильма
    public static void checkDuration(Film film) throws ValidationException {
        if (film.getDuration().isNegative()) {
            log.warn("Введено отрицательно значение для продолжительности фильма ");
            throw new ValidationException("Продолжительности фильма не может быть отрицательной.");
        }
    }


}
