package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Id;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Getter
@Setter
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    // метод для добавления фильма
    @Override
    public Film createFilm(Film film) throws ValidationException {
        ValidationFilm.checkDataOfRelease(film); // проверка даты релиза фильма
        ValidationFilm.checkDuration(film); // проверка продолжительности фильма
        film.setId(Id.getId(films.keySet())); // сгенерировали Id
        log.info("Фильм {} успешно добавлен", film);
        Long id = film.getId();
        films.put(id, film);
        return films.get(id);
    }

    // метод для изменения данных о фильме
    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            Film upbFilm = films.get(film.getId());
            upbFilm.setName(film.getName()); // обновили название фильма
            ValidationFilm.checkDuration(film); // проверка продолжительности фильма
            upbFilm.setDuration(film.getDuration()); // обновили продолжительность фильма
            upbFilm.setDescription(film.getDescription()); // обновили описание фильма
            ValidationFilm.checkDataOfRelease(film); // проверка даты релиза фильма
            upbFilm.setReleaseDate(film.getReleaseDate()); // обновили дату релиза
            films.put(film.getId(), upbFilm); // положили обратно в мапу обновлённые данные
            log.info("Данные фильма {} успешно обновлены", film);
            return upbFilm;
        } else {
            log.warn("Введён неверный id");
            throw new FilmNotFoundException("Фильма с ID " + film.getId() + " нет");
        }
    }

    // метод для получения списка всех фильмов
    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    // получение фильма по id
    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format(Constants.FILM_NOT_EXIST, id));
        }
        return films.get(id);
    }

    @Override
    public String addLike(Long filmId, Long userId) {
        return null;
    }

    @Override
    public String removeLike(Long filmId, Long userId) {
        return null;
    }

    @Override
    public List<Film> mostPopularFilm(Long count) {
        return null;
    }

    // метод удаления фильма
    @Override
    public String removeFilm(Long id) {
        return null;
    }
}
