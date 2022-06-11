package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validate.UserPredicate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userManager) {
        this.userStorage = userManager;
    }

    @Autowired
    private List<UserPredicate> userValidators;

    public Collection<User> getAllUsers() {
            return userStorage.getAllUsers();
    }

    public User getUserByID(long index) {
        if (checkContainUserInUsers(index)) {
            return userStorage.getUserByID(index);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    public User createUser(User user) {
        user = checkingNameUser(user);
        User finalUser = user;
        userValidators.stream()
                .filter(validator -> validator.test(finalUser)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        return userStorage.createUser(user);
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
        return userStorage.updateUser(user);
    }

    public User deleteUser(long index) {
        if (checkContainUserInUsers(index)) {
            return userStorage.deleteUser(index);
        } else {
            throw new FilmOrUserNotFoundException(String.format("Пользователь с id - %d, не найден.", index));
        }
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    public User putFriendToFriends(long idUser, long idFriend) {
        if (checkContainUserInUsers(idFriend) && checkContainUserInUsers(idUser)) {
            userStorage.getUserByID(idUser).getFriends().add(idFriend);
            userStorage.getUserByID(idFriend).getFriends().add(idUser);
        }
        return userStorage.getUserByID(idUser);
    }

    public User deleteFriendFromFriends(long idUser, long idFriend) {
        if (checkContainUserInUsers(idFriend) && checkContainUserInUsers(idUser)) {
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

    public Boolean checkContainUserInUsers(long index) {
        if (userStorage.checkContainUser(index)) {
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
}
