package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    private Integer id;
    @JsonProperty("name")
    private String genreName;
}
