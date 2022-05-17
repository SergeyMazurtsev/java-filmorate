package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectUserBirthdayException extends AllIllegalExceptions {
    public IncorrectUserBirthdayException() {
        super("Дата рождения не может быть в будущем.");
    }
}
