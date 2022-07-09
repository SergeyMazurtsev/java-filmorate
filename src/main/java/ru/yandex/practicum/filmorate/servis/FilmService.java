package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validate.FilmPredicate;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserService userService;

    @Autowired
    public FilmService(@Qualifier("DbFilmH2Storage") FilmStorage filmStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    @Autowired
    private List<FilmPredicate> filmValidators;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long index) {
        if (filmStorage.checkContainFilm(index)) {
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
        if (filmStorage.checkContainFilm(film.getId())) {
            return filmStorage.updateFilm(film);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Фильм с id - %d, не найден.", film.getId()));
        }
    }

    public Film deleteFilm(long index) {
        if (filmStorage.checkContainFilm(index)) {
            return filmStorage.deleteFilm(index);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    public Film addLikeToFilm(long idFilm, long idUser) {
        if (filmStorage.checkContainFilm(idFilm) && userService.checkContainUserInUsers(idUser)) {
            return filmStorage.addLikeToFilm(idFilm, idUser);
        } else {
            throw new FilmOrUserNotFoundException("Пользователь иил фильм не найден.");
        }
    }

    public Film deleteLikeFromFilm(long idFilm, long idUser) {
        if (filmStorage.checkContainFilm(idFilm) && userService.checkContainUserInUsers(idUser)) {
            return filmStorage.deleteLikeFromFilm(idFilm, idUser);
        } else {
            throw new FilmOrUserNotFoundException("Пользователь или фильм не найден.");
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
