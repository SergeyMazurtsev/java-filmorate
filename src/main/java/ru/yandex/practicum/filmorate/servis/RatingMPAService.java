package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.ratingMPA.RatingMPADbStorage;

import java.util.Collection;

@Service
public class RatingMPAService {

    private RatingMPADbStorage ratingMPADbStorage;

    @Autowired
    public RatingMPAService(RatingMPADbStorage ratingMPADbStorage) {
        this.ratingMPADbStorage = ratingMPADbStorage;
    }

    public Collection<RatingMPA> getAllRatingMPA() {
        return ratingMPADbStorage.getAllRatingMPA();
    }

    public RatingMPA getRatingMPAById(Integer id) {
        if (ratingMPADbStorage.checkRatingId(id)) {
            return ratingMPADbStorage.getRatingMPAById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such Rating MPA id - " + id);
        }
    }
}
