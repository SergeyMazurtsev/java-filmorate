package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public UserService(FilmStorage filmManager, UserStorage userManager) {
        this.filmStorage = filmManager;
        this.userStorage = userManager;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserByID(long index) {
        return userStorage.getUserByID(index);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User deleteUser(long index) {
        return userStorage.deleteUser(index);
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    public User putFriendToFriends(long idUser, long idFriend) {
        if (userStorage.checkContainUserInUsers(idFriend) && userStorage.checkContainUserInUsers(idUser)) {
            userStorage.getUserByID(idUser).getFriends().add(idFriend);
            userStorage.getUserByID(idFriend).getFriends().add(idUser);
        }
        return userStorage.getUserByID(idUser);
    }

    public User deleteFriendFromFriends(long idUser, long idFriend) {
        if (userStorage.checkContainUserInUsers(idFriend) && userStorage.checkContainUserInUsers(idUser)) {
            if (userStorage.getUserByID(idUser).getFriends().contains(idFriend)
                    && userStorage.getUserByID(idFriend).getFriends().contains(idUser)) {
                userStorage.getUserByID(idUser).getFriends().remove(idFriend);
                userStorage.getUserByID(idFriend).getFriends().remove(idUser);
            }
        }
        return userStorage.getUserByID(idUser);
    }

    public List<User> getAllFriendsOfUser(long idUser) {
        return userStorage.getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idUser))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsOfUsers(long idUser1, long idUser2) {
        List<User> first = userStorage.getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idUser1)).collect(Collectors.toList());
        List<User> second = userStorage.getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idUser2)).collect(Collectors.toList());
        return first.stream().filter(e -> second.contains(e)).collect(Collectors.toList());
    }
}
