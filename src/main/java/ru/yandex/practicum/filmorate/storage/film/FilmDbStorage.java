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
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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

@Slf4j
@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }


    // создание фильма
    @Override
    public Film createFilm(Film film) throws ValidationException {
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
        String sqlAddFilm = "insert into films(name, description, duration, releasedate) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlAddFilm, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDouble(3, film.getDuration().getSeconds());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            return stmt;
        }, keyHolder);
        Long filmId = keyHolder.getKey().longValue();
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
        String sql = "insert into film_rating(rating_id, film_id) " +
                "values (?, ?)";

        int ratingId = RatingМРАА.getRatingId(film); // проверяем корректный ли рейтинг и получаем id ретинга
        jdbcTemplate.update(sql, ratingId, filmId);
    }

    // добавляет жанр фильма в таблицу genre_genre
    private void saveGenreFilm(Film film, Long filmId) throws GenreNotFoundException {
        String sql = "insert into film_genre(genre_id, film_id) " +
                "values (?, ?)";
        List<Integer> genreId = Genre.getGenreId(film); // проверяем корректный ли жанр и получаем id жанра
        genreId.forEach(id -> jdbcTemplate.update(sql, id, filmId));

    }

    // обновление фильма
    @Override
    public Film updateFilm(Film film) {
        String sql = "update films SET name = ?, description = ?, duration = ?, releasedate = ? where id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("Select * from films where id = ?", film.getId());
        if (userRows.next()) {
            jdbcTemplate.update(sql,
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
        String sql = "update film_genre set genre_id = ?  where film_id = ?";
        //film.getGenre().forEach(g -> jdbcTemplate.update(sql, g, film.getId()));
        List<Integer> genreId = Genre.getGenreId(film); // проверяем корректный ли жанр и получаем id ретинга
        genreId.forEach(id -> jdbcTemplate.update(sql, id, film.getId()));
        log.info("Данные жанра фильма {} успешно обновлены", film.getId());
    }

    // обновление рейтинг фильма
    private void updateFilmRating(Film film) {
        String sql = "update film_rating set rating_id = ?  where film_id = ?";
        int ratingId = RatingМРАА.getRatingId(film); // проверяем корректный ли рейтинг и получаем id ретинга
        jdbcTemplate.update(sql, ratingId, film.getId());
        log.info("Данные рейтинга фильма {} успешно обновлены", film.getId());
    }

    // получение списка всех фильмов
    @Override
    public List<Film> getAllFilms() {
        String sql = "select f.id, f.name, f.description, f.duration, f.releasedate, r.rating, r.id_r from FILMS AS f " +
                "left join film_rating AS fr ON  f.id = fr.film_id " +
                "left join rating AS r ON fr.rating_id = r.id_r";
        return jdbcTemplate.query(sql, (rs, rowNum) -> FilmFromDB.makeFilm(rs, jdbcTemplate));
    }

    // удаление фильма
    @Override
    public String removeFilm(Long id) {
        String sql = "delete from films where id = ?";
        if (jdbcTemplate.update(sql, id) == 0) {
            log.warn("Введён неверный id");
            throw new FilmNotFoundException(String.format(Constants.FILM_NOT_EXIST, id));
        }
        return String.format("Фильм с id %d удалён", id);
    }

    // получение фильма по id
    @Override
    public Film getFilmById(Long id) {
        String sql = "select f.id, f.name, f.description, f.duration, f.releasedate, r.id_r, r.rating " +
                "from FILMS AS f " +
                "left join film_rating AS fr ON  f.id = fr.film_id " +
                "left join rating AS r ON fr.rating_id = r.id_r " +
                "where f.id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (filmRows.next()) {
            log.info("Данные фильма {} успешно получены", id);
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> FilmFromDB.makeFilm(rs, jdbcTemplate), id);
        } else {
            log.warn("Введён неверный id");
            throw new FilmNotFoundException(String.format(Constants.FILM_NOT_EXIST, id));
        }
    }

    //добавление лайка
    @Override
    public String addLike(Long filmId, Long userId) {
        String sql = "insert into likes (user_id, film_id) values (?, ?)";
        String sqlLikes = "select user_id from likes where film_id = ?";
        User user = userStorage.getUsersById(userId);
        Film film = getFilmById(filmId);
        film.setLikes(new HashSet<>(jdbcTemplate.query(sqlLikes, (rs, rowNum) -> FilmFromDB.userId(rs), film.getId())));
        if (film.getLikes().contains(userId)) {
            return String.format(
                    "Пользователь %s уже поставил like фильму %s. Нельзя поставить like более одного раза",
                    user.getLogin(), film.getName());
        } else {
            jdbcTemplate.update(sql,
                    userId,
                    filmId);
            return String.format("Пользователь %s поставил like фильму %s", user.getName(), film.getName());
        }
    }

    // удаление лайка
    @Override
    public String removeLike(Long filmId, Long userId) {
        String sql = "delete from likes where film_id = ? and user_id = ?";
        User user = userStorage.getUsersById(userId);
        Film film = getFilmById(filmId);
        if (jdbcTemplate.update(sql, filmId, userId) == 0) {
            return String.format("Пользователь %s не ставил like фильму %s", user.getLogin(), film.getName());
        }
        return String.format("Like к фильму %d удалён", filmId);
    }

    // вывод наиболее популярных фильмов по количеству лайков.
    @Override
    public List<Film> mostPopularFilm(Long count) {
        String sql = "select f.id, f.name, f.description, f.duration, f.releasedate, r.rating, r.id_r," +
                "count (likes.film_id) as cl " +
                "from films AS f " +
                "LEFT JOIN likes ON  f.id = likes.film_id " +
                "JOIN film_rating AS fr ON  f.id = fr.film_id " +
                "JOIN rating AS r ON fr.rating_id = r.id_r " +
                "group by f.id " +
                "order by cl desc";

        if (count != null) {
            sql += " limit " + count;
        }
        return jdbcTemplate.query(sql, (rs, rowNum) -> FilmFromDB.makeFilm(rs, jdbcTemplate));
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }

}
