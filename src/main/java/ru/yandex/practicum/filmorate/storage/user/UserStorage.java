package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUserByID(long idIndex);

    User createUser(User user);

    User updateUser(User user);

    User deleteUser(long index);

    void deleteAllUsers();

    Boolean checkContainUser(long index);
}
