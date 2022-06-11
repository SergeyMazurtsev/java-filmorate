package ru.yandex.practicum.filmorate.validate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class UserBirthdayValidation implements UserPredicate {
    @Override
    public ValidationException errorObject() {
        return new ValidationException("Дата рождения не может быть в будущем.");
    }

    @Override
    public boolean test(User user) {
        return user.getBirthday().isAfter(LocalDate.now());
    }
}
