package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmDurationException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmLengthDescriptionException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmNameException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servis.FilmManager;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {
    @Autowired
    private FilmManager manager;

    @Autowired
    private FilmController controller;

    private Film film;

    @BeforeEach
    void before() throws JsonProcessingException {
        if (!manager.getAllFilms().isEmpty()) {
            for (Film film : manager.getAllFilms()) {
                manager.deleteFilm(film);
            }
        }
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"name\":\"Victory!\",\"description\":\"Gooood film\",\"releaseDate\":\"2021-01-01\",\"duration\":10000}";
        film = mapper.readValue(str, Film.class);
    }

    @Test
    void controllerLoad() {
        Assertions.assertNotNull(controller);
    }

    @Test
    void getFilms() {
        Assertions.assertEquals(film, controller.create(film));
        Assertions.assertEquals(List.of(film).toString(), controller.findAll().toString());
    }

    @Test
    void updateFilm() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"name\":\"Victory2!\",\"description\":\"Even better film\",\"releaseDate\":\"2021-02-01\",\"duration\":10000}";
        Film film1 = mapper.readValue(str, Film.class);
        Assertions.assertEquals(film1, controller.update(film1));
        Assertions.assertEquals(List.of(film1).toString(), controller.findAll().toString());
    }

    @Test
    void filmNameValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"name\":\"\",\"description\":\"Even better film\",\"releaseDate\":\"2021-02-01\",\"duration\":10000}";
        Film film1 = mapper.readValue(str, Film.class);
        IncorrectFilmNameException exception = Assertions.assertThrows(IncorrectFilmNameException.class,
                () -> {
            controller.create(film1);
                });
        Assertions.assertEquals("Название фильма пустое или отсутствует.", exception.getMessage());
    }

    @Test
    void filmLengthDescriptionValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"name\":\"Victory3!\",\"description\":\"kajhsdkajhsdakjsdhalkdhalkdjhk.jber.kfjn/lai" +
                "jwh.kdjbsdfbvfghvwlekjaadhakjsdmhbnvchgkjfwhk.adljsdnbmnvhjfkqwlkwajsdnbvcghweiuladksjn,bcxhgcsuisfild" +
                "aksjndbchgfiuialjslkdncbnvvhweadl;kncmnbhrygesfliaksdmn\",\"releaseDate\":\"2021-02-01\",\"duration\":10000}";
        Film film1 = mapper.readValue(str, Film.class);
        IncorrectFilmLengthDescriptionException exception = Assertions.assertThrows(IncorrectFilmLengthDescriptionException.class,
                () -> {
            controller.create(film1);
                });
        Assertions.assertEquals("Описание не может быть больше 200 символов.", exception.getMessage());
    }

    @Test
    void filmReleaseDateValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"name\":\"Victory4!\",\"description\":\"Even better film\",\"releaseDate\":\"1790-02-01\",\"duration\":10000}";
        Film film1 = mapper.readValue(str, Film.class);
        IncorrectFilmReleaseDateException exception = Assertions.assertThrows(IncorrectFilmReleaseDateException.class,
                () -> {
            controller.create(film1);
                });
        Assertions.assertEquals("Дата релиза фильма должна быть не раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    void filmDurationValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"name\":\"Victory5!\",\"description\":\"Even better film\",\"releaseDate\":\"2020-10-01\",\"duration\":-10000}";
        Film film1 = mapper.readValue(str, Film.class);
        IncorrectFilmDurationException exception = Assertions.assertThrows(IncorrectFilmDurationException.class,
                () -> {
            controller.create(film1);
                });
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }

}