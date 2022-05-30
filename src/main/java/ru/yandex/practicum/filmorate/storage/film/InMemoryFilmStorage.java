package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validate.FilmPredicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private static Integer idFilm = 0;

    @Autowired
    private List<FilmPredicate> filmValidators;

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film getFilmById(long idIndex) {
        if (films.containsKey(idIndex)) {
            return films.get(idIndex);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Фильм с id - %d, не найден.", idIndex));
        }
    }

    public Film createFilm(Film film) {
        filmValidators.stream()
                .filter(validator -> validator.test(film)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        film.setId(getNextIdFilm());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmValidators.stream()
                .filter(validator -> validator.test(film)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        if (film.getId() < 0) {
            throw new FilmOrUserNotFoundException(String.format("Неверный id - %d.", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film deleteFilm(long index) {
        if (films.containsKey(index)) {
            Film film = films.get(index);
            films.remove(film.getId());
            return film;
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    public void deleteAllFilms() {
        films.clear();
    }

    public Boolean checkContainFilmInFilms(long index) {
        if (films.containsKey(index)) {
            return true;
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    private static Integer getNextIdFilm() {
        return ++idFilm;
    }
}
