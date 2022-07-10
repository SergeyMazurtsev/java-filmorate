package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    @NotNull(message = "MPA can't be null")
    @JsonProperty("mpa")
    private RatingMPA ratingMpa;
    private Set<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", getName());
        result.put("description", getDescription());
        result.put("releasedate", getReleaseDate());
        result.put("duration", getDuration());
        if (getRatingMpa() != null) result.put("rating_mpa", getRatingMpa().getId());
        return result;
    }
}

