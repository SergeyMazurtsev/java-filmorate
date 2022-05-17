package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servis.FilmManager;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmManager filmManager;

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);


    @GetMapping
    public Collection<Film> findAll() {
        return filmManager.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmManager.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmManager.updateFilm(film);
    }

}
