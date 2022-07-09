package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingMPA {
    private int id;
    @JsonProperty("name")
    private String ratingName;
    private String ratingDescription;
}
