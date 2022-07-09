package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validate.UserPredicate;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("DbUserH2Storage") UserStorage userManager) {
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
        User finalUser = user;
        userValidators.stream()
                .filter(validator -> validator.test(finalUser)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        user = checkingNameUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        User finalUser = user;
        userValidators.stream()
                .filter(validator -> validator.test(finalUser)).findFirst()
                .ifPresent(validator -> {
                    throw validator.errorObject();
                });
        user = checkingNameUser(user);
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
            return userStorage.putFriendToFriends(idUser, idFriend);
        } else {
            throw new FilmOrUserNotFoundException("Пользователь не найден.");
        }
    }

    public User deleteFriendFromFriends(long idUser, long idFriend) {
        if (checkContainUserInUsers(idFriend) && checkContainUserInUsers(idUser)) {
            return userStorage.deleteFriendFromFriends(idUser, idFriend);
        } else {
            throw new FilmOrUserNotFoundException("Пользователь не найден.");
        }
    }

    public List<User> getAllFriendsOfUser(long idUser) {
        return userStorage.getAllFriendsOfUser(idUser);
    }

    public List<User> getCommonFriendsOfUsers(long idUser1, long idUser2) {
        return userStorage.getCommonFriendsOfUsers(idUser1, idUser2);
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
