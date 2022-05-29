package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdEnterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmManager;
    private UserStorage userManager;

    @Autowired
    public FilmService(FilmStorage filmManager, UserStorage userManager) {
        this.filmManager = filmManager;
        this.userManager = userManager;
    }

    public Film addLikeToFilm(String idFilm, String idUser) {
        long idF = checkInputIndex(idFilm);
        long idU = checkInputIndex(idUser);

        if (filmManager.checkContainFilmInFilms(idF) && userManager.checkContainUserInUsers(idU)) {
            filmManager.getFilmById(idF).getLikes().add(idU);
        }
        return filmManager.getFilmById(idF);
    }

    public Film deleteLikeFromFilm(String idFilm, String idUser) {
        long idF = checkInputIndex(idFilm);
        long idU = checkInputIndex(idUser);

        if (filmManager.checkContainFilmInFilms(idF) && userManager.checkContainUserInUsers(idU)) {
            if (filmManager.getFilmById(idF).getLikes().contains(idU)) {
                filmManager.getFilmById(idF).getLikes().remove(idU);
            }
        }
        return filmManager.getFilmById(idF);
    }

    public List<Film> getPopularFilms(String count) {
        long i = checkInputIndex(count);

        return filmManager.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(i)
                .collect(Collectors.toList());
    }

    private long checkInputIndex(String index) {
        long i;
        try {
            i = Long.parseLong(index);
        } catch (final NumberFormatException e) {
            throw new IncorrectIdEnterException(String.format("Параметр %s, долже содержать только цифры.",
                    index));
        }
        return i;
    }

}
