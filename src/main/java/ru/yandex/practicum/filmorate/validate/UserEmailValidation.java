package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AllIllegalExceptions;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserEmailException;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserEmailValidation implements UserPredicate {
    @Override
    public AllIllegalExceptions errorObject() {
        return new IncorrectUserEmailException();
    }

    @Override
    public boolean test(User user) {
        return (user.getEmail() == "") || (!user.getEmail().contains("@"));
    }
}
