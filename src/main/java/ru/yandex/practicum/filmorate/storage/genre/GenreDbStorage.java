package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(Integer id) {
        return jdbcTemplate.queryForObject("select id, genre_name from genre where id = ?",
                new MapRowToGenre(), id);
    }

    public Set<Genre> getGenresOfFilm(Long id) {
        String sqlQuery = "select f.genre_id as id, g.genre_name from films_genre as f " +
                "left join genre as g on f.genre_id = g.id where f.film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, new MapRowToGenre(), id))
                .stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toSet());
    }

    public void addGenresOfFilm(Film film) {
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("insert into films_genre (film_id, genre_id)" +
                    "values (?, ?)", film.getId(), genre.getId());
        }
    }

    public void deleteGenresOfFilm(Long id) {
        jdbcTemplate.update("delete from FILMS_GENRE where FILM_ID = ?", id);
    }

    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query("select id, genre_name from GENRE", new MapRowToGenre())
                .stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList());
    }

    public Boolean checkContainGenre(Integer id) {
        List<Integer> request = jdbcTemplate.query("select id, genre_name from genre where id = ?",
                (rs, rowNum) -> rs.getInt(1), id);
        return request.size() != 0;
    }
}
