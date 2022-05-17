package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserEmailException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserLoginException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servis.UserManager;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    UserManager manager;

    @Autowired
    UserController controller;

    private User user;

    @BeforeEach
    void before() throws JsonProcessingException {
        if (!manager.getAllUsers().isEmpty()) {
            for (User user : manager.getAllUsers()) {
                manager.deleteUser(user);
            }
        }
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"email\":\"some@yandex.ru\",\"login\":\"Vasilek\",\"name\":\"Vasya\",\"birthday\":\"1990-01-01\"}";
        user = mapper.readValue(str, User.class);
    }

    @Test
    void controllerLoad() {
        Assertions.assertNotNull(controller);
    }

    @Test
    void getAndCreateUser() {
        Assertions.assertEquals(user, controller.create(user));
        Assertions.assertEquals(List.of(user).toString(), controller.findAllUsers().toString());
    }

    @Test
    void updateFilm() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"email\":\"some@yandex.ru\",\"login\":\"Nik\",\"name\":\"Nikita\",\"birthday\":\"1990-02-01\"}";
        User user1 = mapper.readValue(str, User.class);
        Assertions.assertEquals(user1, controller.updateUser(user1));
        Assertions.assertEquals(List.of(user1).toString(), controller.findAllUsers().toString());
    }

    @Test
    void userEmailValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"email\":\"someyandex.ru\",\"login\":\"Nik\",\"name\":\"Nikita\",\"birthday\":\"1990-02-01\"}";
        User user1 = mapper.readValue(str, User.class);
        String str2 = "{\"id\":1,\"email\":\"\",\"login\":\"Nik\",\"name\":\"Nikita\",\"birthday\":\"1990-02-01\"}";
        User user2 = mapper.readValue(str, User.class);
        IncorrectUserEmailException exception1 = Assertions.assertThrows(IncorrectUserEmailException.class,
                () -> {
            controller.create(user1);
                });
        IncorrectUserEmailException exception2 = Assertions.assertThrows(IncorrectUserEmailException.class,
                () -> {
            controller.create(user2);
                });
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ - @.",
                exception1.getMessage());
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ - @.",
                exception2.getMessage());
    }

    @Test
    void userLoginValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"email\":\"some@yandex.ru\",\"login\":\"\",\"name\":\"Nikita\",\"birthday\":\"1990-02-01\"}";
        User user1 = mapper.readValue(str, User.class);
        String str2 = "{\"id\":1,\"email\":\"some@yandex.ru\",\"login\":\"N i k\",\"name\":\"Nikita\",\"birthday\":\"1990-02-01\"}";
        User user2 = mapper.readValue(str, User.class);
        IncorrectUserLoginException exception1 = Assertions.assertThrows(IncorrectUserLoginException.class,
                () -> {
            controller.create(user1);
                });
        IncorrectUserLoginException exception2 = Assertions.assertThrows(IncorrectUserLoginException.class,
                () -> {
            controller.create(user2);
                });
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.", exception1.getMessage());
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.", exception2.getMessage());
    }

    @Test
    void userNameValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"email\":\"some@yandex.ru\",\"login\":\"Nik\",\"name\":\"\",\"birthday\":\"1990-02-01\"}";
        User user1 = mapper.readValue(str, User.class);
        Assertions.assertEquals("Nik", controller.create(user1).getName());
    }

    @Test
    void userBirthdayValidation() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).build();
        String str = "{\"id\":1,\"email\":\"some@yandex.ru\",\"login\":\"Nik\",\"name\":\"Nikita\",\"birthday\":\"2030-02-01\"}";
        User user1 = mapper.readValue(str, User.class);
        IncorrectUserBirthdayException exception = Assertions.assertThrows(IncorrectUserBirthdayException.class,
                () -> {
            controller.create(user1);
                });
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }
}