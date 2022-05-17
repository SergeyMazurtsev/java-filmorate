package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserBirthdayException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class UserBirthdayValidation implements UserPredicate {
    @Override
    public AllIllegalExceptions errorObject() {
        return new IncorrectUserBirthdayException();
    }

    @Override
    public boolean test(User user) {
        return user.getBirthday().isAfter(LocalDate.now());
    }
}
