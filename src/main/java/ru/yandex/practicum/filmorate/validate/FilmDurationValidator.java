package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmDurationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmDurationValidator implements FilmPredicate {
    @Override
    public AllIllegalExceptions errorObject() {
        return new IncorrectFilmDurationException();
    }

    @Override
    public boolean test(Film film) {
        return film.getDuration().isNegative();
    }
}
