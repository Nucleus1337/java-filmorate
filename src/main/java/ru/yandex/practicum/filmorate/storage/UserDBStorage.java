package ru.yandex.practicum.filmorate.storage;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component("userDBStorage")
public class UserDBStorage implements UserStorage {
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final JdbcTemplate jdbcTemplate;

  public UserDBStorage(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public User add(User user) {
    String query = "insert into \"USER\"(email, login, name, birthday) values(?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(query, new String[] {"id"});
          ps.setString(1, user.getEmail());
          ps.setString(2, user.getLogin());
          ps.setString(3, user.getName());
          ps.setObject(4, user.getBirthday());

          return ps;
        },
        keyHolder);

    user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

    return user;
  }

  @Override
  public User update(User user) {
    jdbcTemplate.update(
        "update \"USER\" set email = ?, login = ?, name = ?, birthday = ? where id = ?",
        user.getEmail(),
        user.getLogin(),
        user.getName(),
        user.getBirthday(),
        user.getId());

    jdbcTemplate.update("delete from friends where user_id = ?", user.getId());

    jdbcTemplate.batchUpdate(
        "insert into friends(user_id, friend_id, confirm) values(?, ?, true)",
        user.getFriends(),
        50,
        (PreparedStatement ps, Long friend) -> {
          ps.setLong(1, user.getId());
          ps.setLong(2, friend);
        });

    return user;
  }

  @Override
  public void remove(Long id) {
    jdbcTemplate.update("delete from \"USER\" where id = ?", id);
  }

  @Override
  public List<User> findAll() {
    return jdbcTemplate.query(
        "select * from \"USER\"",
        (rs, rowNum) -> {
          String friendsQuery =
              String.format(
                  "select friend_id from friends where user_id = %s and confirm = true",
                  rs.getLong("id"));
          List<Long> friends =
              jdbcTemplate.query(
                  friendsQuery, (friendsSet, friendsRowNum) -> friendsSet.getLong(1));

          return new User(
              rs.getLong("id"),
              rs.getString("email"),
              rs.getString("login"),
              rs.getString("name"),
              LocalDate.parse(Objects.requireNonNull(rs.getString("birthday")), formatter),
              new HashSet<>(friends));
        });
  }

  @Override
  public User findById(Long id) {

    SqlRowSet rsUser = jdbcTemplate.queryForRowSet("select * from \"USER\" where id = ?", id);

    if (rsUser.next()) {
      String friendsQuery =
          String.format("select friend_id from friends where user_id = %s and confirm = true", id);
      List<Long> friends = jdbcTemplate.query(friendsQuery, (rs, rowNum) -> rs.getLong(1));

      return new User(
          rsUser.getLong("id"),
          rsUser.getString("email"),
          rsUser.getString("login"),
          rsUser.getString("name"),
          LocalDate.parse(Objects.requireNonNull(rsUser.getString("birthday")), formatter),
          new HashSet<>(friends));
    } else {
      return null;
    }
  }

  @Override
  public long getNextId() {
    return 0;
  }
}
