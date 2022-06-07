package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingМРАА;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private static final String SQL_POPULAR_FILMS = "select f.id, f.name, f.description, f.duration, f.releasedate, r.rating, r.id_r," +
            "count (likes.film_id) as cl " +
            "from films AS f " +
            "LEFT JOIN likes ON  f.id = likes.film_id " +
            "JOIN film_rating AS fr ON  f.id = fr.film_id " +
            "JOIN rating AS r ON fr.rating_id = r.id_r " +
            "group by f.id " +
            "order by cl desc";
    private static final String SQL_DELETE_LIKE = "delete from likes where film_id = ? and user_id = ?";
    private static final String SQL_ADD_LIKE = "insert into likes (user_id, film_id) values (?, ?)";
    private static final String SQL_GET_LIKES = "select user_id from likes where film_id = ?";
    private static final String SQL_FILM_WITH_RATING_BY_ID = "select f.id, f.name, f.description, f.duration, f.releasedate, r.id_r, r.rating " +
            "from FILMS AS f " +
            "left join film_rating AS fr ON  f.id = fr.film_id " +
            "left join rating AS r ON fr.rating_id = r.id_r " +
            "where f.id = ?";
    private static final String SQL_FILM_BY_ID = "Select * from films where id = ?";
    private static final String SQL_REMOVE_FILM = "delete from films where id = ?";
    private static final String SQL_ALL_FILMS = "select f.id, f.name, f.description, f.duration, f.releasedate, r.rating, r.id_r from FILMS AS f " +
            "left join film_rating AS fr ON  f.id = fr.film_id " +
            "left join rating AS r ON fr.rating_id = r.id_r";
    private static final String SQL_UPD_FILM_RATING = "update film_rating set rating_id = ?  where film_id = ?";
    private static final String SQL_UPD_FILM_GENRE = "update film_genre set genre_id = ?  where film_id = ?";
    private static final String SQL_UPD_FILM = "update films SET name = ?, description = ?, duration = ?, releasedate = ? where id = ?";
    private static final String SQL_ADD_FILM_GENRE = "insert into film_genre(genre_id, film_id) " +
            "values (?, ?)";
    private static final String SQL_ADD_FILM_RATING = "insert into film_rating(rating_id, film_id) " +
            "values (?, ?)";
    private static final String SQL_ADD_FILM = "insert into films(name, description, duration, releasedate) " +
            "values (?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    // создание фильма
    @Override
    public Film createFilm(Film film) {
        long id = saveAndReturnId(film); // добавили фильм в таблицу и получили id
        // inMemoryUserStorage.setUsers(id, newFilm);
        return Film.builder()
                .id(id)
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .genre(film.getGenre())
                .mpa(film.getMpa())
                .build();
    }

    // добавляет фильм в таблицу и возвращает Id
    private long saveAndReturnId(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_ADD_FILM, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDouble(3, film.getDuration().getSeconds());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            return stmt;
        }, keyHolder);
        Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        if (film.getMpa() != null) {
            saveRatingFilm(film, filmId); // добавили в отдельную таблицу рейтинг фильма
        }
        if (film.getGenre() != null) {
            saveGenreFilm(film, filmId); // добавили в отдельную таблицу жанр фильма
        }
        return filmId;
    }

    // добавляет рейтинг фильма в таблицу film_rating
    private void saveRatingFilm(Film film, Long filmId) throws RatingNotFoundException {
        int ratingId = RatingМРАА.getRatingId(film); // проверяем корректный ли рейтинг и получаем id ретинга
        jdbcTemplate.update(SQL_ADD_FILM_RATING, ratingId, filmId);
    }

    // добавляет жанр фильма в таблицу genre_genre
    private void saveGenreFilm(Film film, Long filmId) throws GenreNotFoundException {
        List<Integer> genreId = Genre.getGenreId(film); // проверяем корректный ли жанр и получаем id жанра
        genreId.forEach(id -> jdbcTemplate.update(SQL_ADD_FILM_GENRE, id, filmId));

    }

    // обновление фильма
    @Override
    public Film updateFilm(Film film) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(SQL_FILM_BY_ID, film.getId());
        if (userRows.next()) {
            jdbcTemplate.update(SQL_UPD_FILM,
                    film.getName(),
                    film.getDescription(),
                    film.getDuration(),
                    film.getReleaseDate(),
                    film.getId());
            if (film.getGenre() != null) {
                updateFilmGenre(film); // обновили жанр фильма
            }
            if (film.getMpa() != null) {
                updateFilmRating(film); // обновили рейтинг фильма
            }
            log.info("Данные фильма {} успешно обновлены", film.getId());
            log.warn(getFilmById(film.getId()).toString());
            return getFilmById(film.getId());
        } else {
            log.warn("Введён неверный id");
            throw new FilmNotFoundException(String.format(Constants.FILM_NOT_EXIST, film.getId()));
        }
    }

    //обновление жанра фильма
    private void updateFilmGenre(Film film) {
        List<Integer> genreId = Genre.getGenreId(film); // проверяем корректный ли жанр и получаем id ретинга
        genreId.forEach(id -> jdbcTemplate.update(SQL_UPD_FILM_GENRE, id, film.getId()));
        log.info("Данные жанра фильма {} успешно обновлены", film.getId());
    }

    // обновление рейтинг фильма
    private void updateFilmRating(Film film) {
        int ratingId = RatingМРАА.getRatingId(film); // проверяем корректный ли рейтинг и получаем id ретинга
        jdbcTemplate.update(SQL_UPD_FILM_RATING, ratingId, film.getId());
        log.info("Данные рейтинга фильма {} успешно обновлены", film.getId());
    }

    // получение списка всех фильмов
    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(SQL_ALL_FILMS, (rs, rowNum) -> FilmFromDB.makeFilm(rs, jdbcTemplate));
    }

    // удаление фильма
    @Override
    public String removeFilm(Long id) {
        if (jdbcTemplate.update(SQL_REMOVE_FILM, id) == 0) {
            log.warn("Введён неверный id");
            throw new FilmNotFoundException(String.format(Constants.FILM_NOT_EXIST, id));
        }
        return String.format("Фильм с id %d удалён", id);
    }

    // получение фильма по id
    @Override
    public Film getFilmById(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(SQL_FILM_WITH_RATING_BY_ID, id);
        if (filmRows.next()) {
            log.info("Данные фильма {} успешно получены", id);
            return jdbcTemplate.queryForObject(SQL_FILM_WITH_RATING_BY_ID, (rs, rowNum) -> FilmFromDB.makeFilm(rs, jdbcTemplate), id);
        } else {
            log.warn("Введён неверный id");
            throw new FilmNotFoundException(String.format(Constants.FILM_NOT_EXIST, id));
        }
    }

    //добавление лайка
    @Override
    public String addLike(Long filmId, Long userId) {
        User user = userStorage.getUsersById(userId);
        Film film = getFilmById(filmId);
        film.setLikes(new HashSet<>(jdbcTemplate.query(SQL_GET_LIKES, (rs, rowNum) -> FilmFromDB.userId(rs), film.getId())));
        if (film.getLikes().contains(userId)) {
            return String.format(
                    "Пользователь %s уже поставил like фильму %s. Нельзя поставить like более одного раза",
                    user.getLogin(), film.getName());
        } else {
            jdbcTemplate.update(SQL_ADD_LIKE,
                    userId,
                    filmId);
            return String.format("Пользователь %s поставил like фильму %s", user.getName(), film.getName());
        }
    }

    // удаление лайка
    @Override
    public String removeLike(Long filmId, Long userId) {
        User user = userStorage.getUsersById(userId);
        Film film = getFilmById(filmId);
        if (jdbcTemplate.update(SQL_DELETE_LIKE, filmId, userId) == 0) {
            return String.format("Пользователь %s не ставил like фильму %s", user.getLogin(), film.getName());
        }
        return String.format("Like к фильму %d удалён", filmId);
    }

    // вывод наиболее популярных фильмов по количеству лайков.
    @Override
    public List<Film> mostPopularFilm(Long count) {
        if (count != null) {
            String sqlPopularFilmsWithLimit = SQL_POPULAR_FILMS + " limit " + count;
            return jdbcTemplate.query(sqlPopularFilmsWithLimit, (rs, rowNum) -> FilmFromDB.makeFilm(rs, jdbcTemplate));
        }
        return jdbcTemplate.query(SQL_POPULAR_FILMS, (rs, rowNum) -> FilmFromDB.makeFilm(rs, jdbcTemplate));
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }

}
