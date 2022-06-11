package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private static Integer idFilm = 0;

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film getFilmById(long idIndex) {
        return films.get(idIndex);
    }

    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(Film film) {
        film.setId(getNextIdFilm());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film deleteFilm(long index) {
        Film film = films.get(index);
        films.remove(film.getId());
        return film;
    }

    public void deleteAllFilms() {
        films.clear();
    }

    public Boolean checkContainFilm(long index) {
        return films.containsKey(index);
    }

    private static Integer getNextIdFilm() {
        return ++idFilm;
    }
}
