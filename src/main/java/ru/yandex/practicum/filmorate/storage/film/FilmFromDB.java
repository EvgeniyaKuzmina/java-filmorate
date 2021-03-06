package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FilmFromDB {

    public static Long userId(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }

    public static Film makeFilm(ResultSet rs, JdbcTemplate jdbcTemplate) throws SQLException {
        String sqlLikes = "select user_id from likes where film_id = ?";
        String sqlGenre = "select g.genre from genre As g join film_genre AS fg ON g.id = fg.film_id " +
                          "join films AS f ON fg.film_id = f.id "+
                          "where film_id = ?";
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Duration duration = Duration.ofSeconds((long)rs.getDouble("duration"));
        LocalDate releasedate = rs.getDate("releasedate").toLocalDate();
        Map<String, Object> rating = toMap(rs);
        Film film = Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .duration(duration)
                .releaseDate(releasedate)
                .mpa(rating)
                .build();
        film.setLikes(new HashSet<>(jdbcTemplate.query(sqlLikes, (rsLikes, rowNum) -> userId(rsLikes), id)));
        film.setGenre(new ArrayList<>(jdbcTemplate.query(sqlGenre, (rsGenre, rowNum) -> filmGenre(rsGenre), id)));
        return film;
    }

    private static Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        values.put("id", rs.getLong("id_r"));
       // values.put("name", RatingМРАА.gerRatingMPAA(rs.getString("rating")));
        return values;
    }
    
    public static Genre filmGenre(ResultSet rs) throws SQLException {
        return Genre.getGenreName(rs.getString("genre"));
    }
}
