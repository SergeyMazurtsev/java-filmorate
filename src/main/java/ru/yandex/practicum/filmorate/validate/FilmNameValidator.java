package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmNameValidator implements FilmPredicate{
    @Override
    public boolean test(Film film) {
        return (film.getName() == "") || (film.getName() == null);
    }

    @Override
    public ValidationException errorObject() {
        return new ValidationException("Название фильма пустое или отсутствует.");
    }
}
