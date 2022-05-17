package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
public class FilmReleaseDateValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        return film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28));
    }

    @Override
    public AllIllegalExceptions errorObject() {
        return new IncorrectFilmReleaseDateException();
    }
}
