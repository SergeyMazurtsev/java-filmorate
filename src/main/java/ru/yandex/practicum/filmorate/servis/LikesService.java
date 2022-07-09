package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.likes.LikesDbStorage;

import java.util.Set;

@Service
public class LikesService {
    private LikesDbStorage likesDbStorage;

    @Autowired
    public LikesService(LikesDbStorage likesDbStorage) {
        this.likesDbStorage = likesDbStorage;
    }

    public Set<Long> getLikesOfFilm(Long id) {
        return likesDbStorage.getLikesOfFilm(id);
    }
}
