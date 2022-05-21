package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AllIllegalExceptions extends RuntimeException {
    public AllIllegalExceptions(String s) {
        super(s);
    }
}
