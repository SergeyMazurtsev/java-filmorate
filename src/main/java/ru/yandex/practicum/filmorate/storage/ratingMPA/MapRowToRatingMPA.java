package ru.yandex.practicum.filmorate.storage.ratingMPA;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapRowToRatingMPA implements RowMapper<RatingMPA> {
    @Override
    public RatingMPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RatingMPA.builder()
                .id(rs.getInt("id"))
                .ratingName(rs.getString("rating_name"))
                .ratingDescription(rs.getString("rating_description"))
                .build();
    }
}
