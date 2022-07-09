package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servis.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable(name = "id") long index) {
        return ResponseEntity.ok(userService.getUserByID(index));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable(name = "id") long index) {
        return ResponseEntity.ok(userService.deleteUser(index));
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable long id, @PathVariable long friendId) {
        return ResponseEntity.ok(userService.putFriendToFriends(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return ResponseEntity.ok(userService.deleteFriendFromFriends(id, friendId));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<?> getAllFriendOfUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getAllFriendsOfUser(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<?> getCommonFriendsOfUsers(@PathVariable long id, @PathVariable long otherId) {
        return ResponseEntity.ok(userService.getCommonFriendsOfUsers(id, otherId));
    }
}
