package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTest {

    static private User user;
    static private User userWithId;
    static private User updUser;
    static private Film film;
    static private Film updFilm;
    private static Mpa mpa;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @BeforeAll
    static void beforeAll() {
        mpa = Mpa.builder()
                .id(1L)
                .mpa(RatingМРАА.G)
                .build();
        //  mpa.put("name", RatingМРАА.R);
        userWithId = User.builder()
                .id(1L)
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .friends(Set.of())
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        updUser = User.builder()
                .id(2L)
                .email("mail@mail.ru")
                .login("NewDolore")
                .name("NEW Name")
                .birthday(LocalDate.of(1966, 10, 30))
                .build();
        user = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        film = Film.builder()
                .name("film")
                .description("description about film")
                .duration(Duration.ofMinutes(120))
                .releaseDate(LocalDate.of(2000, 1, 12))
                .mpa(mpa)
                .genre(List.of(Genre.DOCUMENTARY))
                .build();

        updFilm = Film.builder()
                .id(2L)
                .name("New film")
                .description("New description about film")
                .duration(Duration.ofMinutes(130))
                .releaseDate(LocalDate.of(2000, 1, 12))
                .mpa(mpa)
                .genre(List.of(Genre.DRAMA))
                .build();
    }

    @BeforeEach
    public void createUserBeforeEach() {
        userStorage.createUser(user); // создаём пользователя 1 в тестовой БД
        userStorage.createUser(user); // создаём пользователя 2 в тестовой БД
        userStorage.createUser(user); // создаём пользователя 3 в тестовой БД
        userStorage.createUser(user); // создаём пользователя 4 в тестовой БД
        userStorage.createUser(user); // создаём пользователя 5 в тестовой БД
        userStorage.createUser(user); // создаём пользователя 6 в тестовой БД
    }

    @BeforeEach
    public void createFilmBeforeEach() throws ValidationException {
        filmStorage.createFilm(film); // создаём фильм 1 в тестовой БД
        filmStorage.createFilm(film); // создаём фильм 2 в тестовой БД
        filmStorage.createFilm(film); // создаём фильм 3 в тестовой БД
        filmStorage.createFilm(film); // создаём фильм 4 в тестовой БД
        filmStorage.createFilm(film); // создаём фильм 5 в тестовой БД
        filmStorage.createFilm(film); // создаём фильм 6 в тестовой БД
    }

    // тестируем методы класса UserDbStorage

    //находим пользователя по Id
    @Test
    public void testFindUserById() {
        Optional<User> userOptional = Optional.of(userStorage.getUsersById(5L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 5L)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Nick Name")
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "dolore")
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                LocalDate.of(1946, 8, 20))
                );
        userStorage.removeUser(5L);
    }

    // обновление данных пользователя
    @Test
    public void testUpdateUser() throws ValidationException {
        Optional<User> userOptional = Optional.of(userStorage.updateUser(updUser)); // обновили данные пользователя 2
        // получили обновлённого юзера
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "NEW Name")
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "NewDolore")
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                LocalDate.of(1966, 10, 30))
                );
    }

    // проверяем удаление пользователя
    @Test
    public void testRemoveUser() {
        userStorage.getUsersById(6L);
        userStorage.removeUser(6L);// удаляем пользователя 6
        Throwable ex = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userStorage.getUsersById(6L) // проверяем что пользователь с id 6 удалён
        );
        assertEquals("Пользователя c id 6 нет", ex.getMessage());
    }

    // проверяем добавление и удаление из друзей
    @Test
    public void testAddAndRemoveFriend() {
        String answerAdd = userStorage.addFriend(3L, 4L); // добавили в друзья пользвателя 4
        assertThat(answerAdd).isEqualTo("Пользователь 3 — Nick Name подписался на пользователя 4 — Nick Name.");
        String answerRemove = userStorage.removeFriend(3L, 4L); // удалили из друзей пользователя 4
        assertThat(answerRemove).isEqualTo("Пользователь 4 удалён из друзей");
    }

    // проверяем получение списка общих друзей
    @Test
    public void testGetCommonFriends() {
        userStorage.addFriend(2L, 1L); // добавили к юзеру 2 в друзья пользвателя 1
        userStorage.addFriend(3L, 1L); // добавили к юзеру 3 в друзья пользвателя 1
        List<User> commonFriends = userStorage.getCommonFriends(2L, 3L);
        commonFriends.forEach(user -> assertThat(user).isEqualTo(userWithId)); //общий друг с id 1
    }

    // проверяем получение списка друзей по id пользователя
    @Test
    public void testGetUserFriendById()  {
        userStorage.addFriend(2L, 1L); // добавили к юзеру 2 в друзья пользвателя 1
        List<User> userFriends = userStorage.getUserFriendById(2L);
        userFriends.forEach(user -> assertThat(user).isEqualTo(userWithId)); //
    }

    // тестируем методы класса FilmDbStorage

    //находим фильм по Id
    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(5L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 5L)
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "film")
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("description", "description about film")
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("likes", Set.of())
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2000, 1, 12))
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("duration",
                                Duration.ofMinutes(120))
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertEquals(user.getMpa(), mpa)
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("genre",
                                List.of(Genre.DOCUMENTARY))
                );
        //filmStorage.removeFilm(5L);
    }

    // обновление данных фильма
    @Test
    public void testUpdateFilm() {
        Optional<Film> filmOptional = Optional.of(filmStorage.updateFilm(updFilm)); // обновили фильм 2
        // получили обновлённого юзера
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L)
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "New film")
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("description", "New description about film")
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("likes", Set.of())
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2000, 1, 12))
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("duration",
                                Duration.ofMinutes(130))
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("mpa",
                                mpa)
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("genre",
                                List.of(Genre.DRAMA))
                );
    }

    // проверяем удаление фильма
    @Test
    public void testRemoveFilm() {
        filmStorage.getFilmById(6L);
        filmStorage.removeFilm(6L);// удаляем фильм 6
        Throwable ex = Assertions.assertThrows(
                FilmNotFoundException.class,
                () -> filmStorage.getFilmById(6L) // проверяем что фильм с id 6 удалён
        );
        assertEquals("Фильма c id 6 нет", ex.getMessage());
    }

    // проверяем добавление и удаление лайка
    @Test
    public void testAddAndRemoveLike() {
        String answerAdd = filmStorage.addLike(1L, 1L); // пользователь 1 поставил лайк фильму 1
        assertThat(answerAdd).isEqualTo("Пользователь Nick Name поставил like фильму film");
        String answerRemove = filmStorage.removeLike(1L, 1L); // удалили лайк
        assertThat(answerRemove).isEqualTo("Like к фильму 1 удалён");
    }

    // проверяем получение списка самых популярных фильмов
    @Test
    public void testMostPopularFilm() {
        String answerAdd = filmStorage.addLike(1L, 4L); // пользователь 4 поставил лайк фильму 1
        List<Film> popularFilms = filmStorage.mostPopularFilm(1L);
        popularFilms.forEach(film -> assertThat(film).isEqualTo(filmStorage.getFilmById(1L)));
    }


}