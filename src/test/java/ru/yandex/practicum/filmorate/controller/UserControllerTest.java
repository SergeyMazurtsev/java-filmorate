package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    UserStorage manager;

    @Autowired
    UserController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    private User user = User.builder().login("Vas").name("Vasya").email("some@yandex.ru")
        .birthday(LocalDate.of(1990,01,01)).build();;
    private User user2 = User.builder().login("Vas2").name("Vasya2").email("some2@yandex.ru")
            .birthday(LocalDate.of(1990,01,01)).build();;
    private User user3 = User.builder().login("Vas3").name("Vasya3").email("some3@yandex.ru")
            .birthday(LocalDate.of(1990,01,01)).build();;

    private final String url = "http://localhost:8080/users";

    @BeforeEach
    void before() throws JsonProcessingException {
        manager.deleteAllUsers();
    }

    @Test
    public void getAllUsers() throws Exception {
        long i1 = createTestUser(user).getId();
        long i2 = createTestUser(user2).getId();
        long i3 = createTestUser(user3).getId();

        mockMvc.perform(MockMvcRequestBuilders
                .get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Vasya"))
                .andExpect(jsonPath("$[1].name").value("Vasya2"))
                .andExpect(jsonPath("$[2].name").value("Vasya3"));
    }

    @Test
    public void createUser() throws Exception {
        mockMvc.perform(post(url).content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Vasya"))
                .andExpect(jsonPath("$.login").value("Vas"))
                .andExpect(jsonPath("$.email").value("some@yandex.ru"))
                .andExpect(status().isOk());
    }

    @Test
    public void putUser() throws Exception {
        createTestUser(user);
        user.setName("Vasya2");
        mockMvc.perform(put(url).content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Vasya2"))
                .andExpect(jsonPath("$.login").value("Vas"))
                .andExpect(jsonPath("$.email").value("some@yandex.ru"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserById() throws Exception {
        long i = createTestUser(user).getId();
        mockMvc.perform(get(url + "/{id}", i))
                .andExpect(jsonPath("$.name").value("Vasya"))
                .andExpect(jsonPath("$.login").value("Vas"))
                .andExpect(jsonPath("$.email").value("some@yandex.ru"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserById() throws Exception {
        long i = createTestUser(user).getId();
        mockMvc.perform(delete(url + "/{id}", i).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Vasya"))
                .andExpect(jsonPath("$.login").value("Vas"))
                .andExpect(jsonPath("$.email").value("some@yandex.ru"))
                .andExpect(status().isOk());
    }

    @Test
    public void addFriend() throws Exception {
        long i = createTestUser(user).getId();
        long i2 = createTestUser(user2).getId();
        mockMvc.perform(put(url +"/{id}/friends/{friendId}", i, i2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.friends").isArray())
                .andExpect(jsonPath("$.friends", is(List.of((int) i2))))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFriend() throws Exception {
        long i = createTestUser(user).getId();
        long i2 = createTestUser(user2).getId();
        controller.addFriend(String.valueOf(i), String.valueOf(i2));

        mockMvc.perform(delete(url + "/{id}/friends/{friendId}", i, i2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.friends").isArray())
                .andExpect(jsonPath("$.friends", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllFriends() throws Exception {
        long i = createTestUser(user).getId();
        long i2 = createTestUser(user2).getId();
        long i3 = createTestUser(user3).getId();
        controller.addFriend(String.valueOf(i), String.valueOf(i2));
        controller.addFriend(String.valueOf(i), String.valueOf(i3));

        mockMvc.perform(get(url +"/{id}/friends", i).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Vasya2"))
                .andExpect(jsonPath("$[1].name").value("Vasya3"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCommonFriends() throws Exception {
        long i = createTestUser(user).getId();
        long i2 = createTestUser(user2).getId();
        long i3 = createTestUser(user3).getId();
        controller.addFriend(String.valueOf(i), String.valueOf(i2));
        controller.addFriend(String.valueOf(i), String.valueOf(i3));

        mockMvc.perform(get(url +"/{id}/friends/common/{otherId}", i2, i3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Vasya"))
                .andExpect(status().isOk());

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private User createTestUser(User u) {
        return manager.createUser(u);
    }
}