package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servis.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserStorage userManager;
    private UserService userService;

    @Autowired
    public UserController(UserStorage userManager, UserService userService) {
        this.userManager = userManager;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userManager.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable(name = "id") long index) {
        return userManager.getUserByID(index);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userManager.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userManager.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUserById(@PathVariable(name = "id") long index) {
        return userManager.deleteUser(index);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userManager.deleteAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.putFriendToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.deleteFriendFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriendOfUser(@PathVariable String id) {
        return userService.getAllFriendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsOfUsers(@PathVariable String id, @PathVariable String otherId) {
        return userService.getCommonFriendsOfUsers(id, otherId);
    }
}
