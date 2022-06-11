package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servis.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable(name = "id") long index) {
        return filmService.getFilmById(index);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilmById(@PathVariable long id) {
        return filmService.deleteFilm(id);
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
    }

    @PutMapping("{id}/like/{userId}")
    public Film postLikeToFilmByUser(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLikeToFilm(id,userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film deleteLikeFromFile(@PathVariable long id, @PathVariable long userId) {
        return filmService.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") @Positive int count) {
        return filmService.getPopularFilms(count);
    }
}
