package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long index) {
        return filmStorage.getFilmById(index);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film deleteFilm(long index) {
        return filmStorage.deleteFilm(index);
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    public Film addLikeToFilm(long idFilm, long idUser) {
        if (filmStorage.checkContainFilmInFilms(idFilm) && userStorage.checkContainUserInUsers(idUser)) {
            filmStorage.getFilmById(idFilm).getLikes().add(idUser);
        }
        return filmStorage.getFilmById(idFilm);
    }

    public Film deleteLikeFromFilm(long idFilm, long idUser) {
        if (filmStorage.checkContainFilmInFilms(idFilm) && userStorage.checkContainUserInUsers(idUser)) {
            if (filmStorage.getFilmById(idFilm).getLikes().contains(idUser)) {
                filmStorage.getFilmById(idFilm).getLikes().remove(idUser);
            }
        }
        return filmStorage.getFilmById(idFilm);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
