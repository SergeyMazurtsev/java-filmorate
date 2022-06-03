package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private static long idUser = 0;

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
        user.setId(getNextIdUser());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User deleteUser(long index) {
        User user = users.get(index);
        users.remove(index);
        return user;
    }

    public void deleteAllUsers() {
        users.clear();
    }

    public Boolean checkContainUser(long index) {
        return users.containsKey(index);
    }

    private static long getNextIdUser() {
        return ++idUser;
    }
}
