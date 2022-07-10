package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film getFilmById(long idIndex);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(long index);

    void deleteAllFilms();

    Film addLikeToFilm(long idFilm, long idUser);

    Film deleteLikeFromFilm(long idFilm, long idUser);

    public List<Film> getPopularFilms(int count);

    Boolean checkContainFilm(long index);
}
