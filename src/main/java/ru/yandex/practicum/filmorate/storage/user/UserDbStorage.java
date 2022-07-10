package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component("DbUserH2Storage")
public class UserDbStorage implements UserStorage {
    private JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query("select id, email, login, name, birthday from users",
                new MapRowToUser());
    }

    @Override
    public User getUserByID(long idIndex) {
        User user = jdbcTemplate.queryForObject("select id, email, login, name, birthday from users where id = ?",
                new MapRowToUser(), idIndex);
        List<Long> friends = jdbcTemplate.query("select second_friend " +
                        "from friends where first_friend = ? and status = ?", (rs, rowNum) -> rs.getLong(1),
                idIndex, true);
        if (friends.size() != 0) {
            user.setFriends(new HashSet<>(friends));
        }
        return user;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users").usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("update users set email = ?, login = ?, name = ?, birthday = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public User deleteUser(long index) {
        User user = getUserByID(index);
        jdbcTemplate.update("delete from users where id = ?", index);
        return user;
    }

    @Override
    public void deleteAllUsers() {
        jdbcTemplate.update("delete from users");
    }

    @Override
    public User putFriendToFriends(long idUser, long idFriend) {
        User secondFriend = getUserByID(idFriend);
        Boolean status = false;
        if (secondFriend.getFriends() != null) {
            if (secondFriend.getFriends().contains(idUser)) {
                status = true;
                jdbcTemplate.update("update friends set first_friend = ?, second_friend = ?, status = ? " +
                        "where first_friend = ?, second_friend = ?", idFriend, idUser, status, idFriend, idUser);
            }
        }
        jdbcTemplate.update("insert into friends (first_friend, second_friend, status) " +
                "values (?, ?, ?)", idUser, idFriend, status);
        return getUserByID(idUser);
    }

    @Override
    public User deleteFriendFromFriends(long idUser, long idFriend) {
        User secondFriend = getUserByID(idFriend);
        if (secondFriend.getFriends() != null) {
            if (secondFriend.getFriends().contains(idUser)) {
                jdbcTemplate.update("update friends set first_friend = ?, second_friend = ?, status = ? " +
                        "where first_friend = ?, second_friend = ?", idFriend, idUser, false, idFriend, idUser);
            }
        }
        jdbcTemplate.update("delete from friends where first_friend = ? and second_friend = ?",
                idUser, idFriend);
        return getUserByID(idUser);
    }

    @Override
    public List<User> getAllFriendsOfUser(long idUser) {
        return jdbcTemplate.query("select f.second_friend, u.email, u.login, u.name, u.birthday from friends as f " +
                "inner join users as u on f.second_friend = u.id where f.first_friend = ?", (rs, rowNum) -> User.builder()
                .id(rs.getLong("second_friend"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build(), idUser);
    }

    @Override
    public List<User> getCommonFriendsOfUsers(long idUser1, long idUser2) {
        return jdbcTemplate.query("select id, email, login, name, birthday from users " +
                " where id in (select second_friend from friends where first_friend = ? " +
                "intersect select second_friend from friends where first_friend = ?)",
                (rs, rowNum) -> User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build(), idUser1, idUser2);
    }

    @Override
    public Boolean checkContainUser(long index) {
        List<Long> request = jdbcTemplate.query("select id from users where id=?",
                (rs, rowNum) -> rs.getLong(1), index);
        return request.size() != 0;
    }
}
