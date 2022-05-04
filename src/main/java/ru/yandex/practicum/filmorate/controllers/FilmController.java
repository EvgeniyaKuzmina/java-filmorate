package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Component
@RequestMapping("/films")
public class FilmController {
    private InMemoryFilmStorage inMemoryFilmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    // добавление нового фильма
    @PostMapping
    public String createFilm(@Valid @RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.createFilm(film);
    }

    //обновление существующего фильма
    @PutMapping
    public String updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.updateFilm(film);
    }

    // получение всех фильмов
    @GetMapping
    public Map<Integer, Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    //получение фильма по id
    @GetMapping(value = "{id}")
    @ResponseBody
    public Film getFilmById(@Valid @PathVariable(required = false) String id) throws ValidationException {
        if (id == null) {
            throw new ValidationException(Constants.FILM_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0) {
            throw new FilmNotFoundException(Constants.FILM_ID_INCORRECT);
        }
        return inMemoryFilmStorage.getFilmById(Integer.parseInt(id));
    }

    // Добавление в друзья пользователя по Id
    @PutMapping(value = {"/{id}/like/{userId}", "/{id}/like/", "/like/{userId}", "/like/"})
    @ResponseBody
    public String addLikeToFilm(@Valid @PathVariable(required = false) String id,
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
        return filmService.addLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    //удаление пользователя из друзей по id
    @DeleteMapping(value = {"/{id}/like/{userId}", "/{id}/like/", "/like/{userId}", "/like/"})
    @ResponseBody
    public String removeLikeFromFilm(@Valid @PathVariable(required = false) String id,
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
        return filmService.removeLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    // получение списка самых популярных фильмов
    @GetMapping("/popular")
    @ResponseBody
    public HashMap<String, String> mostPopularFilm(@Valid @RequestParam(defaultValue = "10", required = false) Integer count) throws
                                                                                                                              ValidationException {
        if (count == 0 || count < 1) {
            throw new ValidationException("Ошибка ввода поля count");
        }
        return filmService.mostPopularFilm(count);
    }


}
