package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationExceptionFilm;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Id;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    public Map<Integer, Film> getFilms() {
        return films;
    }

    // добавление нового фильма
    @PostMapping(value = "/film")
    public String createFilm(@Valid @RequestBody Film film) {
        try {
            ValidationException.checkName(film); // проверка названия фильма
            ValidationExceptionFilm.checkDescription(film); // проверка описания фильма
            ValidationExceptionFilm.checkDataOfRelease(film); // проверка даты релиза фильма
            ValidationExceptionFilm.checkDuration(film); // проверка продолжительности фильма
            film.setFilmId(Id.getId(films.keySet())); // сгенерировали Id

            log.info("Фильм {} успешно добавлен", film);
            films.put(film.getFilmId(), film);
            return "Фильм " + film.getFilmName() + " успешно добавлен";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    //обновление существующего фильма
    @PutMapping(value = "/film")
    public String updateFilm(@RequestBody Film film) {
        try {
            if (films.containsKey(film.getFilmId())) {
                Film upbFilm = films.get(film.getFilmId());
                ValidationExceptionFilm.checkDescription(film); // проверка описания фильма
                upbFilm.setDescription(film.getDescription()); // обновили описание фильма
                ValidationExceptionFilm.checkDataOfRelease(film); // проверка даты релиза фильма
                upbFilm.setDateOfRelease(film.getDateOfRelease()); // обновили дату релиза
                films.put(film.getFilmId(), upbFilm); // положили обратно в мапу обновлённые данные
                log.info("Данные фильма {} успешно обновлены", film);
                return "Данные фильма " + film.getFilmName() + " успешно обновлены";
            } else {
                log.warn("Введён неверный id");
                throw new ValidationException("Фильма с ID " + film.getFilmId() + " нет");
            }
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    // получение всех фильмов
    @GetMapping("/films")
    public Map<Integer, Film> getAllFilms() {
        return films;
    }


}
