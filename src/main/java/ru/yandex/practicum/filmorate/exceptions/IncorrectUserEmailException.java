package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectUserEmailException extends AllIllegalExceptions {
    public IncorrectUserEmailException() {
        super("Электронная почта не может быть пустой и должна содержать символ - @.");
    }
}
