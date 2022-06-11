package ru.yandex.practicum.filmorate.validate;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.function.Predicate;

public interface UserPredicate extends Predicate<User> {
    ValidationException errorObject();
}
