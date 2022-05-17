package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectUserLoginException extends AllIllegalExceptions {
    public IncorrectUserLoginException() {
        super("Логин не может быть пустым и содержать пробелы.");
    }
}
