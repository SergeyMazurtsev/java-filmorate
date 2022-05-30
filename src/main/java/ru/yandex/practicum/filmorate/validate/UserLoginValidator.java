package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserLoginValidator implements UserPredicate {
    @Override
    public ValidationException errorObject() {
        return new ValidationException("Логин не может быть пустым и содержать пробелы.");
    }

    @Override
    public boolean test(User user) {
        return (user.getLogin() == "") || (user.getLogin().contains(" "));
    }
}
