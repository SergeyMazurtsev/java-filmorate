package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("filmImMemoryStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private static Long idFilm;

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film getFilmById(long idIndex) {
        return films.get(idIndex);
    }

    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(Film film) {
        film.setId(getNextIdFilm());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film deleteFilm(long index) {
        Film film = films.get(index);
        films.remove(film.getId());
        return film;
    }

    public void deleteAllFilms() {
        films.clear();
    }

    @Override
    public Film addLikeToFilm(long idFilm, long idUser) {
        getFilmById(idFilm).getLikes().add(idUser);
        return getFilmById(idFilm);
    }

    @Override
    public Film deleteLikeFromFilm(long idFilm, long idUser) {
        if (getFilmById(idFilm).getLikes().contains(idUser)) {
            getFilmById(idFilm).getLikes().remove(idUser);
        }
        return getFilmById(idFilm);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Boolean checkContainFilm(long index) {
        return films.containsKey(index);
    }

    private static Long getNextIdFilm() {
        return ++idFilm;
    }
}
