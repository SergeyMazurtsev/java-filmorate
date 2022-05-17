package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validate.FilmPredicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmManager {
    private Map<Integer, Film> films = new HashMap<>();

    @Autowired
    List<FilmPredicate> filmValidators;

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film createFilm(Film film) {
        final var filmErrorValidator = filmValidators.stream()
                .filter(validator -> validator.test(film)).findFirst();
        filmErrorValidator.ifPresent(validator -> {
            throw validator.errorObject();
        });
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        final var filmErrorValidator = filmValidators.stream()
                .filter(validator -> validator.test(film)).findFirst();
        filmErrorValidator.ifPresent(validator -> {
            throw validator.errorObject();
        });
        films.put(film.getId(), film);
        return film;
    }

    public Film deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
        }
        return film;
    }
}
