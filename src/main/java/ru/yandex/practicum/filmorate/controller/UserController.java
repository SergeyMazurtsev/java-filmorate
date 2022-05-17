package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servis.UserManager;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserManager userManager;

    @GetMapping
    public Collection<User> findAllUsers() {
        return userManager.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userManager.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userManager.updateUser(user);
    }
}
