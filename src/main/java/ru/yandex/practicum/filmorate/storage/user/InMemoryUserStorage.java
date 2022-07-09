package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("userImMemoryStorage")
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

    @Override
    public User putFriendToFriends(long idUser, long idFriend) {
        getUserByID(idUser).getFriends().add(idFriend);
        getUserByID(idFriend).getFriends().add(idUser);
        return getUserByID(idUser);
    }

    @Override
    public User deleteFriendFromFriends(long idUser, long idFriend) {
        if (getUserByID(idUser).getFriends().contains(idFriend)
                && getUserByID(idFriend).getFriends().contains(idUser)) {
            getUserByID(idUser).getFriends().remove(idFriend);
            getUserByID(idFriend).getFriends().remove(idUser);
        }
        return getUserByID(idUser);
    }

    @Override
    public List<User> getAllFriendsOfUser(long idUser) {
        return getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriendsOfUsers(long idUser1, long idUser2) {
        List<User> first = getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idUser1)).collect(Collectors.toList());
        List<User> second = getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idUser2)).collect(Collectors.toList());
        return first.stream().filter(e -> second.contains(e)).collect(Collectors.toList());
    }

    public Boolean checkContainUser(long index) {
        return users.containsKey(index);
    }

    private static long getNextIdUser() {
        return ++idUser;
    }
}
