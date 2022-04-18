package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class ValidationExceptionFilm extends ValidationException {
    private static final LocalDate EARLIEST_DATA_OF_RELEASE = LocalDate.of(1895, 12, 28);


    public ValidationExceptionFilm(String messages) {
        super(messages);
    }

    // проверка описания фильма
    public static void checkDescription(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            log.warn("Описание фильма слишком большое,");
            throw new ValidationExceptionFilm("В описании фильма " + film.getDescription().length() + " символов." +
                                                      " Допустимое количество символом в описании 200");
        }
    }

    // проверка даты релиза
    public static void checkDataOfRelease(Film film) throws ValidationException {
        if (film.getDateOfRelease().isBefore(EARLIEST_DATA_OF_RELEASE)) {
            log.warn("Дата релиза указана ранее {}", EARLIEST_DATA_OF_RELEASE);
            throw new ValidationExceptionFilm("Вы не можете указать дату релиза ранее " + EARLIEST_DATA_OF_RELEASE);
        }
    }

    // проверка продолжительности фильма
    public static void checkDuration(Film film) throws ValidationException {
        if (film.getDuration().isNegative()) {
            log.warn("Введено отрицательно значение для продолжительности фильма ");
            throw new ValidationExceptionFilm("Продолжительности фильма не может быть отрицательной.");
        }
    }


}