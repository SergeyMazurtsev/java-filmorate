package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUserByID(long idIndex);

    User createUser(User user);

    User updateUser(User user);

    User deleteUser(long index);

    void deleteAllUsers();

    User putFriendToFriends(long idUser, long idFriend);

    User deleteFriendFromFriends(long idUser, long idFriend);

    List<User> getAllFriendsOfUser(long idUser);

    List<User> getCommonFriendsOfUsers(long idUser1, long idUser2);

    Boolean checkContainUser(long index);
}
