package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validate.FilmPredicate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    @Autowired
    private List<FilmPredicate> filmValidators;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long index) {
        if (checkContainFilmInFilms(index)) {
            return filmStorage.getFilmById(index);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Фильм с id - %d, не найден.", index));
        }
    }

    public Film createFilm(Film film) {
        filmValidators.stream()
                .filter(validator -> validator.test(film)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        return filmStorage.createFilm(film);
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
        return filmStorage.updateFilm(film);
    }

    public Film deleteFilm(long index) {
        if (checkContainFilmInFilms(index)) {
            return filmStorage.deleteFilm(index);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    public Film addLikeToFilm(long idFilm, long idUser) {
        if (checkContainFilmInFilms(idFilm) && userService.checkContainUserInUsers(idUser)) {
            filmStorage.getFilmById(idFilm).getLikes().add(idUser);
        }
        return filmStorage.getFilmById(idFilm);
    }

    public Film deleteLikeFromFilm(long idFilm, long idUser) {
        if (checkContainFilmInFilms(idFilm) && userService.checkContainUserInUsers(idUser)) {
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

    public Boolean checkContainFilmInFilms(long index) {
        if (filmStorage.checkContainFilm(index)) {
            return true;
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

}
