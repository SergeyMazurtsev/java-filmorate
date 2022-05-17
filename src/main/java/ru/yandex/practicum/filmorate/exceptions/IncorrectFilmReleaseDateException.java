package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectFilmReleaseDateException extends AllIllegalExceptions {
    public IncorrectFilmReleaseDateException() {
        super("Дата релиза фильма должна быть не раньше 28 декабря 1895 года.");
    }
}
