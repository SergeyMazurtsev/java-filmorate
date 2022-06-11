package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
public class FilmReleaseDateValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        return film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28));
    }

    @Override
    public ValidationException errorObject() {
        return new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года.");
    }
}
