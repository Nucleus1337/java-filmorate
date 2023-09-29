package ru.yandex.practicum.filmorate.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

@Component("filmDBStorage")
@RequiredArgsConstructor
public class FilmDBStorage implements FilmStorage {
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final JdbcTemplate jdbcTemplate;
  private final MpaService mpaService;
  private final GenreService genreService;

  @Override
  public Film add(Film film) {
    String query =
        "insert into film(name, description, release_date, duration, rating_id) values(?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(query, new String[] {"id"});
          ps.setString(1, film.getName());
          ps.setString(2, film.getDescription());
          ps.setObject(3, film.getReleaseDate());
          ps.setLong(4, film.getDuration());
          ps.setLong(5, film.getMpa().getId());

          return ps;
        },
        keyHolder);

    long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();

    if (!film.getGenres().isEmpty()) {
      jdbcTemplate.batchUpdate(
          "insert into films_genres(film_id, genre_id) values(?, ?)",
          film.getGenres(),
          50,
          (ps, genre) -> {
            ps.setLong(1, filmId);
            ps.setLong(2, genre.getId());
          });
    }

    film.setId(filmId);

    return film;
  }

  @Override
  public Film update(Film film) {

    jdbcTemplate.update(
        "update film set name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? where id = ?",
        film.getName(),
        film.getDescription(),
        film.getReleaseDate(),
        film.getDuration(),
        film.getMpa().getId(),
        film.getId());

    jdbcTemplate.update("delete from films_genres where film_id = ?", film.getId());

    if (film.getGenres() != null) {
      if (!film.getGenres().isEmpty())
        jdbcTemplate.batchUpdate(
            "insert into films_genres(film_id, genre_id) values(?, ?)",
            film.getGenres(),
            50,
            (ps, genre) -> {
              ps.setLong(1, film.getId());
              ps.setLong(2, genre.getId());
            });

      Comparator<Genre> comparator = Comparator.comparing(Genre::getId);
      TreeSet<Genre> newSet = new TreeSet<>(comparator);
      newSet.addAll(film.getGenres());

      film.setGenres(newSet);
    }

    jdbcTemplate.update("delete from likes where film_id = ?", film.getId());

    if (film.getLikes() != null)
      if (!film.getLikes().isEmpty())
        jdbcTemplate.batchUpdate(
            "insert into likes(film_id, user_id) values(?, ?)",
            film.getLikes(),
            50,
            (ps, userLike) -> {
              ps.setLong(1, film.getId());
              ps.setLong(2, userLike);
            });

    return film;
  }

  @Override
  public void remove(Long id) {
    jdbcTemplate.update("delete from film where id = ?", id);
  }

  @Override
  public List<Film> findAll() {
    List<Film> films = new ArrayList<>();
    List<Mpa> mpas = mpaService.findAll();
    List<Genre> genres = genreService.findAll();

    SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from film");

    while (rs.next()) {
      films.add(findById(rs.getLong("id")));
      //      Mpa mpa = null;
      //      Optional<Mpa> mpaOptional =
      //          mpas.stream().filter(m -> m.getId() == rs.getLong("id")).findFirst();
      //
      //      if (mpaOptional.isPresent()) mpa = mpaOptional.get();
      //
      //      films.add(
      //          new Film(
      //              rs.getLong("id"),
      //              rs.getString("name"),
      //              rs.getString("description"),
      //              LocalDate.parse(Objects.requireNonNull(rs.getString("release_date")),
      // FORMATTER),
      //              rs.getLong("duration"),
      //              new HashSet<>(),
      //              mpa,
      //              new HashSet<>()));
    }

    return films;
  }

  @Override
  public Film findById(Long id) {
    SqlRowSet rsFilm = jdbcTemplate.queryForRowSet("select * from film where id = ?", id);
    if (rsFilm.next()) {
      String queryLikes = String.format("select user_id from likes where film_id = %s", id);
      String queryGenres =
          String.format(
              "select genre_id, name from films_genres f, genres g where film_id = %s and g.id = f.genre_id",
              id);
      String queryMpa =
          String.format(
              "select rating_id, r.name from film f, ratings r where f.id = %s and r.id = f.rating_id",
              id);

      List<Long> likes = jdbcTemplate.query(queryLikes, (rs, rowNum) -> rs.getLong(1));
      List<Genre> genres =
          jdbcTemplate.query(
              queryGenres, (rs, rowNum) -> new Genre(rs.getLong(1), rs.getString(2)));
      List<Mpa> mpas =
          jdbcTemplate.query(queryMpa, (rs, rowNum) -> new Mpa(rs.getLong(1), rs.getString(2)));

      return new Film(
          id,
          rsFilm.getString("name"),
          rsFilm.getString("description"),
          LocalDate.parse(Objects.requireNonNull(rsFilm.getString("release_date")), FORMATTER),
          rsFilm.getLong("duration"),
          new HashSet<>(likes),
          mpas.get(0),
          new HashSet<>(genres));
    } else {
      return null;
    }
  }

  @Override
  public long getNextId() {
    return 0;
  }
}
