package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserLoginException;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserLoginValidator implements UserPredicate {
    @Override
    public AllIllegalExceptions errorObject() {
        return new IncorrectUserLoginException();
    }

    @Override
    public boolean test(User user) {
        return (user.getLogin() == "") || (user.getLogin().contains(" "));
    }
}
