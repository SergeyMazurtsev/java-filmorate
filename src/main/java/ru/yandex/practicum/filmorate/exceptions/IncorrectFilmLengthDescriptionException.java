package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectFilmLengthDescriptionException extends AllIllegalExceptions {
    public IncorrectFilmLengthDescriptionException() {
        super("Описание не может быть больше 200 символов.");
    }
}
