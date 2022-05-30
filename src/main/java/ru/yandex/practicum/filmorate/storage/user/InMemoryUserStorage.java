package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserPredicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private static long idUser = 0;

    @Autowired
    private List<UserPredicate> userValidators;

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User getUserByID(long idIndex) {
        if (users.containsKey(idIndex)) {
            return users.get(idIndex);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", idIndex));
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(User user) {
        user = checkingNameUser(user);
        User finalUser = user;
        userValidators.stream()
                .filter(validator -> validator.test(finalUser)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        user.setId(getNextIdUser());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        user = checkingNameUser(user);
        User finalUser = user;
        userValidators.stream()
                .filter(validator -> validator.test(finalUser)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        if (user.getId() < 0) {
            throw new FilmOrUserNotFoundException(String.format("Неверный id - %d.", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    public User deleteUser(long index) {
        if (users.containsKey(index)) {
            User user = users.get(index);
            users.remove(index);
            return user;
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    public void deleteAllUsers() {
        users.clear();
    }

    public Boolean checkContainUserInUsers(long index) {
        if (users.containsKey(index)) {
            return true;
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    public User checkingNameUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private static long getNextIdUser() {
        return ++idUser;
    }
}
