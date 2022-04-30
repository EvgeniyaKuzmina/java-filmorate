package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Id;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Getter
@Setter
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();


    // метод для добавления фильма
    public String createFilm(Film film) throws ValidationException {
        ValidationFilm.checkDataOfRelease(film); // проверка даты релиза фильма
        ValidationFilm.checkDuration(film); // проверка продолжительности фильма
        film.setFilmId(Id.getId(films.keySet())); // сгенерировали Id
        log.info("Фильм {} успешно добавлен", film);
        films.put(film.getFilmId(), film);
        return "Фильм " + film.getFilmName() + " успешно добавлен";
    }

    // метод для изменения данных о фильме
    public String updateFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getFilmId())) {
            Film upbFilm = films.get(film.getFilmId());
            upbFilm.setDescription(film.getDescription()); // обновили описание фильма
            ValidationFilm.checkDataOfRelease(film); // проверка даты релиза фильма
            upbFilm.setReleaseDate(film.getReleaseDate()); // обновили дату релиза
            films.put(film.getFilmId(), upbFilm); // положили обратно в мапу обновлённые данные
            log.info("Данные фильма {} успешно обновлены", film);
            return "Данные фильма " + film.getFilmName() + " успешно обновлены";
        } else {
            log.warn("Введён неверный id");
            throw new ValidationException("Фильма с ID " + film.getFilmId() + " нет");
        }
    }

    // метод для получения списка всех фильмов
    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    // метод удаления фильма
    public String removeFilm(Film film) {return null; }
}
