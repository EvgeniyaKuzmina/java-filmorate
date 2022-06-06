package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;


@Slf4j
@Service

public class ValidationFilm {

    private static final LocalDate EARLIEST_DATA_OF_RELEASE = LocalDate.of(1895, 12, 28);


    private final FilmStorage filmStorage;

    public ValidationFilm(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

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

    public void checkFilmById(Long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильма c id %d нет", id));
        }
    }


}
