package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servis.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

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
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findFilmById(@PathVariable(name = "id") long index) {
        return ResponseEntity.ok(filmService.getFilmById(index));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(filmService.createFilm(film));
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilmById(@PathVariable long id) {
        return ResponseEntity.ok(filmService.deleteFilm(id));
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<?> postLikeToFilmByUser(@PathVariable long id, @PathVariable long userId) {
        return ResponseEntity.ok(filmService.addLikeToFilm(id,userId));
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<?> deleteLikeFromFile(@PathVariable long id, @PathVariable long userId) {
        return ResponseEntity.ok(filmService.deleteLikeFromFilm(id, userId));
    }

    @GetMapping("popular")
    public ResponseEntity<?> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") @Positive int count) {
        return ResponseEntity.ok(filmService.getPopularFilms(count));
    }
}
