package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectFilmNameException extends AllIllegalExceptions {
    public IncorrectFilmNameException() {
        super("Название фильма пустое или отсутствует.");
    }
}
