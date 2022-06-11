package ru.yandex.practicum.filmorate.exceptions;

public class FilmOrUserNotFoundException extends ValidationException {
    public FilmOrUserNotFoundException(String s) {
        super(s);
    }
}
