package ru.yandex.practicum.filmorate.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class GenreService {
    private GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreStorage) {
        this.genreDbStorage = genreStorage;
    }

    public Set<Genre> getGenresOfFilm(Long id) {
        return new HashSet<>(genreDbStorage.getGenresOfFilm(id));
    }

    public Genre getGenreById(Integer id) {
        if (genreDbStorage.checkContainGenre(id)) {
            return genreDbStorage.getGenreById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no genre with such id - " + id);
        }
    }

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public void deleteGenreOfFilm(Long id) {
        genreDbStorage.deleteGenresOfFilm(id);
    }

    public void addGenreOfFilm(Film film) {
        genreDbStorage.addGenresOfFilm(film);
    }
}
