package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final ResultSetExtractor<Map<Integer, User>> EXTRACTOR = (ResultSetExtractor<Map<Integer, User>>) rs -> {
        Map<Integer, User> users = new HashMap<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            if (users.containsKey(id)) {
                users.get(id).addRole(Role.valueOf(rs.getString(9)));
            } else {
                User user = new User(id, rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(7), rs.getBoolean(6),
                        rs.getDate(5), List.of(Role.valueOf(rs.getString(9))));
                users.put(id, user);
            }
        }
        return users;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        List<Role> roles = new ArrayList<>(user.getRoles());
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        deleteRoles(user.id());
        createRoles(roles, user.id());
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        Map<Integer, User> users = jdbcTemplate.query("SELECT * FROM users " +
                "LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", EXTRACTOR, id);
        if (users == null || users.size() == 0)
            return null;
        else
            return users.values().stream().findFirst().orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        Map<Integer, User> users = jdbcTemplate.query("SELECT * FROM users " +
                "LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", EXTRACTOR, email);
        if (users == null || users.size() == 0)
            return null;
        else
            return users.values().stream().findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", EXTRACTOR).values().
                stream().collect(Collectors.toList());
    }

    private void createRoles(List<Role> roles, int userId) {
        jdbcTemplate.batchUpdate("INSERT INTO user_roles values (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, roles.get(i).toString());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }

    private void deleteRoles(int userId) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", userId);
    }
}
