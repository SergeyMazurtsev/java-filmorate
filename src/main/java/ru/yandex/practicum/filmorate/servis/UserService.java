package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdEnterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private FilmStorage filmManager;
    private UserStorage userManager;

    @Autowired
    public UserService(FilmStorage filmManager, UserStorage userManager) {
        this.filmManager = filmManager;
        this.userManager = userManager;
    }

    public User putFriendToFriends(String idUser, String idFriend) {
        long idU = checkInputIndex(idUser);
        long idF = checkInputIndex(idFriend);

        if (userManager.checkContainUserInUsers(idF) && userManager.checkContainUserInUsers(idU)) {
            userManager.getUserByID(idU).getFriends().add(idF);
            userManager.getUserByID(idF).getFriends().add(idU);
        }
        return userManager.getUserByID(idU);
    }

    public User deleteFriendFromFriends(String idUser, String idFriend) {
        long idU = checkInputIndex(idUser);
        long idF = checkInputIndex(idFriend);

        if (userManager.checkContainUserInUsers(idF) && userManager.checkContainUserInUsers(idU)) {
            if (userManager.getUserByID(idU).getFriends().contains(idF)
                    && userManager.getUserByID(idF).getFriends().contains(idU)) {
                userManager.getUserByID(idU).getFriends().remove(idF);
                userManager.getUserByID(idF).getFriends().remove(idU);
            }
        }
        return userManager.getUserByID(idU);
    }

    public List<User> getAllFriendsOfUser(String idUser) {
        long id = checkInputIndex(idUser);
        return userManager.getAllUsers().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsOfUsers(String idUser1, String idUser2) {
        long idFirst = checkInputIndex(idUser1);
        long idSecond = checkInputIndex(idUser2);
        List<User> first = userManager.getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idFirst)).collect(Collectors.toList());
        List<User> second = userManager.getAllUsers().stream()
                .filter(user -> user.getFriends().contains(idSecond)).collect(Collectors.toList());
        return first.stream().filter(e -> second.contains(e)).collect(Collectors.toList());
    }

    private long checkInputIndex(String index) {
        long i;
        try {
            i = Long.parseLong(index);
        } catch (final NumberFormatException e) {
            throw new IncorrectIdEnterException(String.format("Параметр %s, долже содержать только цифры.",
                    index));
        }
        return i;
    }
}
