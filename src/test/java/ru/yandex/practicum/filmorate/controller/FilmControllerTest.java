package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private FilmStorage manager;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private FilmController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    private final Film film = Film.builder().name("Victory").description("Gooood film")
            .releaseDate(LocalDate.of(2021, 10, 10)).duration(10000).build();
    private final Film film2 = Film.builder().name("Victory2").description("Gooood film")
            .releaseDate(LocalDate.of(2021, 10, 10)).duration(10000).build();
    private final Film film3 = Film.builder().name("Victory3").description("Gooood film")
            .releaseDate(LocalDate.of(2021, 10, 10)).duration(10000).build();

    private final String url = "http://localhost:8080/films";

    @BeforeEach
    void before() throws JsonProcessingException {
        manager.deleteAllFilms();
        userStorage.deleteAllUsers();
    }

    @Test
    void getAllFilms() throws Exception {
        long i1 = createTestFilm(film).getId();
        long i2 = createTestFilm(film2).getId();
        long i3 = createTestFilm(film3).getId();

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Victory")))
                .andExpect(jsonPath("$[1].name", is("Victory2")))
                .andExpect(jsonPath("$[2].name", is("Victory3")));

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createFilm() throws Exception {
        mockMvc.perform(post(url).content(mapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Victory")));
    }

    @Test
    public void getFilmById() throws Exception {
        long i1 = createTestFilm(film).getId();

        mockMvc.perform(get(url + "/{id}", i1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Victory")));
    }

    @Test
    public void updateFilm() throws Exception {
        Film f1 = Film.builder().name(film3.getName()).description(film3.getDescription())
                .releaseDate(film3.getReleaseDate()).duration(film3.getDuration()).build();
        long i = createTestFilm(f1).getId();
        f1.setName("Victory another");
        f1.setId(i);

        mockMvc.perform(put(url).content(mapper.writeValueAsString(f1)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Victory another")));
    }

    @Test
    public void deleteFilmById() throws Exception {
        long i1 = createTestFilm(film).getId();

        mockMvc.perform(delete(url + "/{id}", i1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Victory")))
                .andExpect(status().isOk());
    }

    @Test
    public void postAndDelLikeToFilmByUser() throws Exception {
        User user = User.builder().login("Vas").name("Vasya").email("some@yandex.ru")
                .birthday(LocalDate.of(1990, 01, 01)).build();
        long u = userStorage.createUser(user).getId();
        long i1 = createTestFilm(film).getId();

        mockMvc.perform(put(url + "/{id}/like/{userId}", i1, u)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.likes").isArray())
                .andExpect(jsonPath("$.likes", hasSize(1)));

        mockMvc.perform(delete(url + "/{id}/like/{userId}", i1, u).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.likes").isArray())
                .andExpect(jsonPath("$.likes", hasSize(0)));
    }

    @Test
    public void getPopularFilms() throws Exception {
        long f1 = createTestFilm(film).getId();
        long f2 = createTestFilm(film2).getId();
        long f3 = createTestFilm(film3).getId();
        long u1 = userStorage.createUser(User.builder().login("Vas").name("Vasya").email("some@yandex.ru")
                .birthday(LocalDate.of(1990, 01, 01)).build()).getId();
        long u2 = userStorage.createUser(User.builder().login("Vas2").name("Vasya2").email("some@yandex.ru")
                .birthday(LocalDate.of(1990, 01, 01)).build()).getId();
        long u3 = userStorage.createUser(User.builder().login("Vas3").name("Vasya3").email("some@yandex.ru")
                .birthday(LocalDate.of(1990, 01, 01)).build()).getId();
        long u4 = userStorage.createUser(User.builder().login("Vas4").name("Vasya4").email("some@yandex.ru")
                .birthday(LocalDate.of(1990, 01, 01)).build()).getId();
        long u5 = userStorage.createUser(User.builder().login("Vas5").name("Vasya5").email("some@yandex.ru")
                .birthday(LocalDate.of(1990, 01, 01)).build()).getId();

        controller.postLikeToFilmByUser(String.valueOf(f1), String.valueOf(u1));
        controller.postLikeToFilmByUser(String.valueOf(f1), String.valueOf(u2));
        controller.postLikeToFilmByUser(String.valueOf(f1), String.valueOf(u3));
        controller.postLikeToFilmByUser(String.valueOf(f1), String.valueOf(u4));
        controller.postLikeToFilmByUser(String.valueOf(f1), String.valueOf(u5));
        controller.postLikeToFilmByUser(String.valueOf(f2), String.valueOf(u2));
        controller.postLikeToFilmByUser(String.valueOf(f2), String.valueOf(u3));
        controller.postLikeToFilmByUser(String.valueOf(f2), String.valueOf(u4));
        controller.postLikeToFilmByUser(String.valueOf(f2), String.valueOf(u5));
        controller.postLikeToFilmByUser(String.valueOf(f3), String.valueOf(u4));
        controller.postLikeToFilmByUser(String.valueOf(f3), String.valueOf(u5));

        mockMvc.perform(get(url + "/popular").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Victory")))
                .andExpect(jsonPath("$[1].name", is("Victory2")))
                .andExpect(jsonPath("$[2].name", is("Victory3")))
                .andExpect(status().isOk());
    }

    private Film createTestFilm(Film u) {
        return manager.createFilm(u);
    }
}