package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class IncorrectFilmDurationException extends AllIllegalExceptions {
    public IncorrectFilmDurationException() {
        super("Продолжительность фильма должна быть положительной.");
    }
}
