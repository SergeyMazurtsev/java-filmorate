package ru.yandex.practicum.filmorate.validate;

import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.function.Predicate;

public interface FilmPredicate extends Predicate<Film> {
    AllIllegalExceptions errorObject();
}
