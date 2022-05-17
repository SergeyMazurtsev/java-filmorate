package ru.yandex.practicum.filmorate.validate;

import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.model.User;

import java.util.function.Predicate;

public interface UserPredicate extends Predicate<User> {
    AllIllegalExceptions errorObject();
}
