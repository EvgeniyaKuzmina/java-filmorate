package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmDbService filmService;

    @Autowired
    public FilmController(@Qualifier("FilmDbService") FilmDbService filmService) {
        this.filmService = filmService;
    }

    // добавление нового фильма
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.createFilm(film);
    }

    //обновление существующего фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    // получение всех фильмов
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    //получение фильма по id
    @GetMapping(value = "{id}")
    @ResponseBody
    public Film getFilmById(@PathVariable(required = false) String id) throws ValidationException {
        if (id == null) {
            throw new ValidationException(Constants.FILM_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0) {
            throw new FilmNotFoundException(Constants.FILM_ID_INCORRECT);
        }
        return filmService.getFilmById(Long.parseLong(id));
    }

    // Добавление лайка к фильму
    @PutMapping(value = {"/{id}/like/{userId}", "/{id}/like/", "/like/{userId}", "/like/"})
    @ResponseBody
    public String addLikeToFilm(@PathVariable(required = false) String id,
                                @PathVariable(required = false) String userId) throws ValidationException {
        if (id == null) {
            throw new ValidationException(Constants.FILM_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0) {
            throw new FilmNotFoundException(Constants.FILM_ID_INCORRECT);
        }
        if (userId == null) {
            throw new ValidationException(Constants.USER_ID_IS_EMPTY);
        }
        if (Integer.parseInt(userId) <= 0) {
            throw new FilmNotFoundException(Constants.USER_ID_INCORRECT);
        }
        return filmService.addLike(Long.parseLong(id), Long.parseLong(userId));
    }

    //удаление лайка из фильма
    @DeleteMapping(value = {"/{id}/like/{userId}", "/{id}/like/", "/like/{userId}", "/like/"})
    @ResponseBody
    public String removeLikeFromFilm(@PathVariable(required = false) String id,
                                     @PathVariable(required = false) String userId) throws ValidationException {
        if (id == null) {
            throw new ValidationException(Constants.FILM_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0) {
            throw new FilmNotFoundException(Constants.FILM_ID_INCORRECT);
        }
        if (userId == null) {
            throw new ValidationException(Constants.USER_ID_IS_EMPTY);
        }
        if (Integer.parseInt(userId) <= 0) {
            throw new UserNotFoundException(Constants.USER_ID_INCORRECT);
        }
        return filmService.removeLike(Long.parseLong(id), Long.parseLong(userId));
    }

    // получение списка самых популярных фильмов
    @GetMapping("/popular")
    @ResponseBody
    public List<Film> mostPopularFilm(@RequestParam(defaultValue = "10", required = false) Long count) throws
            ValidationException {
        if (count == 0 || count < 1) {
            throw new ValidationException("Ошибка ввода поля count");
        }
        return filmService.mostPopularFilm(count);
    }

    @DeleteMapping(value = {"/{id}"})
    @ResponseBody
    public String removeUser(@PathVariable(required = false) String id) {
        return filmService.removeFilm(Long.parseLong(id));
    }


}
