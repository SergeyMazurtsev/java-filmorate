package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.servis.GenreService;
import ru.yandex.practicum.filmorate.servis.LikesService;
import ru.yandex.practicum.filmorate.servis.RatingMPAService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("DbFilmH2Storage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private GenreService genreService;
    private RatingMPAService ratingMPAService;
    private LikesService likesService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService, RatingMPAService ratingMPAService,
                         LikesService likesService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
        this.ratingMPAService = ratingMPAService;
        this.likesService = likesService;
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> request = jdbcTemplate.query("select id, name, description, releaseDate, duration, rating_MPA " +
                "from films", new MapRowToFilm());
        request.stream().forEach(film -> {
            film.setRatingMpa(ratingMPAService.getRatingMPAById(film.getRatingMpa().getId()));
        });
        return request;
    }

    @Override
    public Film getFilmById(long idIndex) {
        Film film = jdbcTemplate.queryForObject("select id, name, description, releaseDate, duration, rating_MPA " +
                "from films where id = ?", new MapRowToFilm(), idIndex);
        film.setRatingMpa(ratingMPAService.getRatingMPAById(film.getRatingMpa().getId()));
        Set<Genre> genres = genreService.getGenresOfFilm(film.getId());
        if (!genres.isEmpty()) {
            film.setGenres(genres);
        }
        Set<Long> likes = likesService.getLikesOfFilm(film.getId());
        if (!likes.isEmpty()) {
            film.setLikes(likes);
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films").usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        film.setRatingMpa(ratingMPAService.getRatingMPAById(film.getRatingMpa().getId()));
        if (film.getGenres() != null) {
            genreService.addGenreOfFilm(film);
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("update films set name = ?, description = ?, releaseDate = ?, duration = ?, rating_MPA = ? " +
                        "where id = ?", film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRatingMpa().getId(), film.getId());
        if (!genreService.getGenresOfFilm(film.getId()).isEmpty()) {
            genreService.deleteGenreOfFilm(film.getId());
        }
        if (film.getGenres() != null) {
            genreService.addGenreOfFilm(film);
        }
        film = getFilmById(film.getId());
        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }
        return film;
    }

    @Override
    public Film deleteFilm(long index) {
        Film film = getFilmById(index);
        jdbcTemplate.update("delete from films where id = ?", index);
        return film;
    }

    @Override
    public void deleteAllFilms() {
        jdbcTemplate.update("delete from films");
    }

    @Override
    public Film addLikeToFilm(long idFilm, long idUser) {
        jdbcTemplate.update("insert into likes (film_id, user_id) values (?, ?)",
                idFilm, idUser);
        return getFilmById(idFilm);
    }

    @Override
    public Film deleteLikeFromFilm(long idFilm, long idUser) {
        Film film = getFilmById(idFilm);
        jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", idFilm, idUser);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query("select f.ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING_MPA " +
                        "from FILMS as f left join LIKES L on f.ID = L.FILM_ID " +
                        "group by f.ID order by count(l.USER_ID) desc limit ?",
                new MapRowToFilm(), count);
    }

    @Override
    public Boolean checkContainFilm(long index) {
        List<Integer> request = jdbcTemplate.query("select id from films where id=?",
                (rs, rowNum) -> rs.getInt(1), index);
        return request.size() != 0;
    }
}
