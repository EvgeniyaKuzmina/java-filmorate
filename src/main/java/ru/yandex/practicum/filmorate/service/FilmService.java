package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.List;

import static ru.yandex.practicum.filmorate.constant.Constants.STANDARD_SIZE;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;
    private final ValidationFilm validationFilm;
    private final ValidationUser validationUser;

    //добавление лайка
    public String addLike(Integer filmId, Integer userId) {
        validationFilm.checkFilmById(filmId);
        validationUser.checkUserById(userId);
        User user = inMemoryUserStorage.getUsers().get(userId);
        Film film = inMemoryFilmStorage.getFilms().get(filmId);
        if (film.getLikes().contains(user)) { // проверяем ставил ли пользователь лайк фильма ранее
            return String.format("Пользователь %s уже поставил like фильму %s. Нельзя поставить like более одного раза",
                                 user.getLogin(), film.getFilmName());
        }
        film.setLike(user);
        return String.format("Пользователь %s поставил like фильму %s", user.getName(), film.getFilmName());
    }

    // удаление лайка
    public String removeLike(Integer filmId, Integer userId) {
        validationFilm.checkFilmById(filmId);
        validationUser.checkUserById(userId);
        User user = inMemoryUserStorage.getUsers().get(userId);
        Film film = inMemoryFilmStorage.getFilms().get(filmId);
        if (!film.getLikes().contains(user)) { // проверяем ставил ли пользователь лайк фильма ранее
            return String.format("Пользователь %s не ставил Like фильму %s", user.getLogin(), film.getFilmName());
        }
        film.getLikes().remove(user);
        return String.format("Пользователь %s удалил Like к фильму %s", user.getLogin(), film.getFilmName());
    }

    // вывод наиболее популярных фильмов по количеству лайков.
    public List<Film> mostPopularFilm(Integer count) throws ValidationException {
        List<Film> sortedFilms = inMemoryFilmStorage.getFilms().values().stream()
                                                    .sorted(this::compare)
                                                    .limit(count)
                                                    .toList();
        if (count > sortedFilms.size() && sortedFilms.size() < STANDARD_SIZE) {
            return sortedFilms;
        } else if (count > sortedFilms.size()) {
            return sortedFilms.stream()
                              .limit(STANDARD_SIZE)
                              .toList();
        } else {
            return sortedFilms.stream()
                              .limit(count)
                              .toList();
        }
    }

    private int compare(Film f1, Film f2) {
        return f1.getLikes().size() - (f2.getLikes().size()); //прямой порядок сортировки
    }


    //TODO
    // — вывод 10 наиболее популярных фильмов по количеству лайков.
    // —Пусть пока каждый пользователь может поставить лайк фильму только один раз.


}
