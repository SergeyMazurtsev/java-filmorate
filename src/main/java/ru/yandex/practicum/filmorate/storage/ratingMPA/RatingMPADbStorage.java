package ru.yandex.practicum.filmorate.storage.ratingMPA;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.Collection;
import java.util.List;

@Component
public class RatingMPADbStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingMPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<RatingMPA> getAllRatingMPA() {
        return jdbcTemplate.query("select id, rating_name, rating_description " +
                "from rating_mpa", new MapRowToRatingMPA());
    }

    public RatingMPA getRatingMPAById(Integer id) {
        return jdbcTemplate.queryForObject("select id, rating_name, rating_description " +
                "from RATING_MPA where ID = ?", new MapRowToRatingMPA(), id);
    }

    public Boolean checkRatingId(Integer id) {
        List<Integer> request = jdbcTemplate.query("select id from rating_MPA where id = ?",
                (rs, rowNum) -> rs.getInt(1), id);
        return request.size() != 0;
    }
}
