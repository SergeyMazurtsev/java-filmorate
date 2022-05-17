package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmLengthDescriptionException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmDescriptionLengthValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        return (film.getDescription().length() > 200) || (film.getDescription() == "");
    }

    @Override
    public AllIllegalExceptions errorObject() {
        return new IncorrectFilmLengthDescriptionException();
    }
}
