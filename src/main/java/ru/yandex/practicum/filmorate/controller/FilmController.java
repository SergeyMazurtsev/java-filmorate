package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servis.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmStorage filmManager;
    private FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmManager, FilmService filmService) {
        this.filmManager = filmManager;
        this.filmService = filmService;
    }

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        return filmManager.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable(name = "id") long index) {
        return filmManager.getFilmById(index);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmManager.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmManager.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilmById(@PathVariable long id) {
        return filmManager.deleteFilm(id);
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmManager.deleteAllFilms();
    }

    @PutMapping("{id}/like/{userId}")
    public Film postLikeToFilmByUser(@PathVariable String id, @PathVariable String userId) {
        return filmService.addLikeToFilm(id,userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film deleteLikeFromFile(@PathVariable String id, @PathVariable String userId) {
        return filmService.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") String count) {
        return filmService.getPopularFilms(count);
    }
}
