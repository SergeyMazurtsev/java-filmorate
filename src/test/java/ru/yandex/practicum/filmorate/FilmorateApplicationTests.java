package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class FilmorateApplicationTests {

    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    private final Film firstFilm = Film.builder()
            .name("First")
            .description("First Film")
            .releaseDate(LocalDate.of(2011, 01, 01))
            .duration(210)
            .ratingMpa(RatingMPA.builder().id(1).ratingName("G").build())
            .build();
    private final Film secondFilm = Film.builder()
            .name("Second")
            .description("Second Film")
            .releaseDate(LocalDate.of(2012, 02, 02))
            .duration(220)
            .ratingMpa(RatingMPA.builder().id(2).ratingName("PG").build())
            .build();
    private final User firstUser = User.builder()
            .email("first@user.ru")
            .login("first")
            .name("Man")
            .birthday(LocalDate.of(1985, 05, 05))
            .build();
    private final User secondUser = User.builder()
            .email("second@user.ru")
            .login("second")
            .name("Woman")
            .birthday(LocalDate.of(1985, 05, 15))
            .build();

    @Test
    public void testCreateAndGetUser() {
        User testUser = userDbStorage.createUser(firstUser);
        Optional<User> optionalUser = Optional.ofNullable(userDbStorage.getUserByID(testUser.getId()));
        assertThat(optionalUser).isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", testUser.getId())
                        .hasFieldOrPropertyWithValue("name", "Man")
                );
    }

    @Test
    public void testUpdateUser() {
        User testUser = userDbStorage.createUser(firstUser);
        testUser.setName("Updated first");
        Optional<User> testOptional = Optional.ofNullable(userDbStorage.updateUser(testUser));
        assertThat(testOptional).isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", testUser.getId())
                        .hasFieldOrPropertyWithValue("name", testUser.getName())
                );
    }

    @Test
    public void testGetAllUsers() {
        User testUser1 = userDbStorage.createUser(firstUser);
        User testUser2 = userDbStorage.createUser(secondUser);
        Collection<User> optionalUsers = userDbStorage.getAllUsers();
        assertThat(optionalUsers, hasItem(testUser1));
        assertThat(optionalUsers, hasItem(testUser2));
    }

    @Test
    public void testDeleteUser() {
        User testUser = userDbStorage.createUser(firstUser);
        userDbStorage.deleteUser(testUser.getId());
        assertThat(userDbStorage.getAllUsers(), empty());
    }

    @Test
    public void testCreateAndGetFilm() {
        Film testFilm = filmDbStorage.createFilm(firstFilm);
        Optional<Film> optionalFilm = Optional.ofNullable(filmDbStorage.getFilmById(testFilm.getId()));
        assertThat(optionalFilm).isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", testFilm.getId())
                        .hasFieldOrPropertyWithValue("name", "First")
                );
    }

    @Test
    public void testUpdateFilm() {
        Film testFilm = filmDbStorage.createFilm(firstFilm);
        testFilm.setName("Updated first");
        Optional<Film> testOptional = Optional.ofNullable(filmDbStorage.updateFilm(testFilm));
        assertThat(testOptional).isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", testFilm.getId())
                        .hasFieldOrPropertyWithValue("name", testFilm.getName())
                );
    }

    @Test
    public void testGetAllFilms() {
        Film testFilm1 = filmDbStorage.createFilm(firstFilm);
        Film testFilm2 = filmDbStorage.createFilm(secondFilm);
        Collection<Film> optionalTest = filmDbStorage.getAllFilms();
        assertThat(optionalTest, hasItem(testFilm1));
        assertThat(optionalTest, hasItem(testFilm2));
    }

    public void testDeleteFilm() {
        Film testFilm = filmDbStorage.createFilm(firstFilm);
        filmDbStorage.deleteFilm(testFilm.getId());
        assertThat(filmDbStorage.getAllFilms(), empty());
    }
}
