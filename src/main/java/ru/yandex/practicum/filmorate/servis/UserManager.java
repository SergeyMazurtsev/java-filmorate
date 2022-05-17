package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserPredicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserManager {
    Map<Integer, User> users = new HashMap<>();

    @Autowired
    List<UserPredicate> userValidators;

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User createUser(User user) {
        User finalUser = user;
        final var userError = userValidators.stream()
                .filter(validator -> validator.test(finalUser)).findFirst();
        userError.ifPresent(validator -> {
            throw validator.errorObject();
        });
        user = checkingNameUser(user);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        User finalUser = user;
        final var userError = userValidators.stream()
                .filter(validator -> validator.test(finalUser)).findFirst();
        userError.ifPresent(validator -> {
            throw validator.errorObject();
        });
        user = checkingNameUser(user);
        users.put(user.getId(), user);
        return user;
    }

    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
        }
        return user;
    }

    public User checkingNameUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
