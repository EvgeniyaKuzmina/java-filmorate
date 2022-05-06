package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.List;
import java.util.stream.Collectors;

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
        if (film.getLikes() != null) {
            if (film.getLikes().contains(user)) { // проверяем ставил ли пользователь лайк фильму ранее
                return String.format(
                        "Пользователь %s уже поставил like фильму %s. Нельзя поставить like более одного раза",
                        user.getLogin(), film.getName());
            }
        }
        film.setLike(user);
        return String.format("Пользователь %s поставил like фильму %s", user.getName(), film.getName());
    }

    // удаление лайка
    public String removeLike(Integer filmId, Integer userId) {
        validationFilm.checkFilmById(filmId);
        validationUser.checkUserById(userId);
        User user = inMemoryUserStorage.getUsers().get(userId);
        Film film = inMemoryFilmStorage.getFilms().get(filmId);
        if (film.getLikes() != null) {
            if (!film.getLikes().contains(user)) { // проверяем ставил ли пользователь лайк фильму ранее
                return String.format("Пользователь %s не ставил like фильму %s", user.getLogin(), film.getName());
            }
            film.getLikes().remove(user);
            return String.format("Пользователь %s удалил like к фильму %s", user.getLogin(), film.getName());
        }
        return String.format("У фильма %s нет лайков", film.getName());
    }

    // вывод наиболее популярных фильмов по количеству лайков.
    public List<Film> mostPopularFilm(Integer count) {
        List<Film> sortedFilms = inMemoryFilmStorage.getFilms().values().stream()
                                                    .sorted(this::compare)
                                                    .collect(Collectors.toList());
        if (count > sortedFilms.size() && sortedFilms.size() < STANDARD_SIZE) {
            return sortedFilms;
        } else if (count > sortedFilms.size()) {
            return sortedFilms.stream()
                              .limit(STANDARD_SIZE).collect(Collectors.toList());

        } else {
            return sortedFilms.stream()
                              .limit(count)
                              .collect(Collectors.toList());

        }
    }

    private int compare(Film f1, Film f2) {
        return  (f2.getLikes().size() - f1.getLikes().size()); //прямой порядок сортировки
    }


}
