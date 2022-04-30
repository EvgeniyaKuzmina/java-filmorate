package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Component
public class FilmController {
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    // добавление нового фильма
    @PostMapping(value = "/films")
    public String createFilm(@Valid @RequestBody Film film) throws ValidationException {

        return inMemoryFilmStorage.createFilm(film);
    }

    //обновление существующего фильма
    @PutMapping(value = "/films")
    public String updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.updateFilm(film);

    }

    // получение всех фильмов
    @GetMapping("/films")
    public Map<Integer, Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @GetMapping("/films")
    public List<Film> mostPopularFilm(Integer count) throws ValidationException {
        if (count == 0 || count < 1) {
            throw new ValidationException("Ошибка ввода поля count");
        }
        return null;
    }


}
