package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserEmailValidation implements UserPredicate {
    @Override
    public ValidationException errorObject() {
        return new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @.");
    }

    @Override
    public boolean test(User user) {
        return (user.getEmail() == "") || (!user.getEmail().contains("@"));
    }
}
