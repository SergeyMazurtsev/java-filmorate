package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmDurationValidator implements FilmPredicate {
    @Override
    public ValidationException errorObject() {
        return new ValidationException("Продолжительность фильма должна быть положительной.");
    }

    @Override
    public boolean test(Film film) {
        return (film.getDuration() < 0);
    }
}
