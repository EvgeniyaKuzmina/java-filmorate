package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
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
        film.setId(Id.getId(films.keySet())); // сгенерировали Id
        log.info("Фильм {} успешно добавлен", film);
        films.put(film.getId(), film);
        return "Фильм " + film.getName() + " успешно добавлен";
    }

    // метод для изменения данных о фильме
    public String updateFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            Film upbFilm = films.get(film.getId());
            upbFilm.setDescription(film.getDescription()); // обновили описание фильма
            ValidationFilm.checkDataOfRelease(film); // проверка даты релиза фильма
            upbFilm.setReleaseDate(film.getReleaseDate()); // обновили дату релиза
            films.put(film.getId(), upbFilm); // положили обратно в мапу обновлённые данные
            log.info("Данные фильма {} успешно обновлены", film);
            return "Данные фильма " + film.getName() + " успешно обновлены";
        } else {
            log.warn("Введён неверный id");
            throw new FilmNotFoundException("Фильма с ID " + film.getId() + " нет");
        }
    }

    // метод для получения списка всех фильмов
    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    // метод удаления фильма
    public String removeFilm(Film film) {
        return null;
    }
}
