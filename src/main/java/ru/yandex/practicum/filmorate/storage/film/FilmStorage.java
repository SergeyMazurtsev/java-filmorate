package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film getFilmById(long idIndex);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(long index);

    void deleteAllFilms();

    Boolean checkContainFilm(long index);
}
