package task.test.working.groups.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task.test.working.groups.data.User;
import task.test.working.groups.data.enums.UserRole;
import task.test.working.groups.repositories.api.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static task.test.working.groups.data.enums.UserRole.OWNER_GROUP;
import static task.test.working.groups.data.enums.UserRole.USER;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final NamedParameterJdbcTemplate template;

    private static final String SQL_INSERT =
        "INSERT INTO users (id, name, role) VALUES (:ID, :NAME, :ROLE) " +
            "ON CONFLICT (id) DO UPDATE SET name = excluded.name, role = excluded.role";
    private static final String SQL_QUERY_BY_ID =
        "SELECT id, name, role FROM users WHERE id = :ID";
    private static final String SQL_QUERY_BY_ROLE_AND_GROUP =
        "SELECT users.id, users.name, users.role FROM users, user_to_group " +
            "WHERE users.role = :ROLE AND user_to_group.group_id = :GROUP_ID";
    private static final String SQL_DELETE =
        "DELETE FROM users WHERE id = :ID";

    private static final String SQL_RESET_ROLE =
        "UPDATE users SET role = :ROLE WHERE id in (" +
            "SELECT users.id FROM users, user_to_group WHERE user_to_group.group_id = :GROUP_ID)";

    @Override
    public Optional<User> getUser(Integer id) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("ID", id);

        return template.query(SQL_QUERY_BY_ID, sqlParameters, getResultDtoRowMapper())
            .stream()
            .findAny();
    }

    @Override
    public Optional<User> getUserOwner(Integer groupId) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("ROLE", OWNER_GROUP.name())
            .addValue("GROUP_ID", groupId);

        return template.query(SQL_QUERY_BY_ROLE_AND_GROUP, sqlParameters, getResultDtoRowMapper())
            .stream()
            .findAny();
    }

    @Override
    public void updateUser(User user) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("ID", user.getId())
            .addValue("NAME", user.getName())
            .addValue("ROLE", user.getRole().name());

        template.update(SQL_INSERT, sqlParameters);
    }

    @Override
    public void deleteUser(Integer id) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("ID", id);

        template.update(SQL_DELETE, sqlParameters);
    }

    @Override
    public void resetUserRoleByGroupId(Integer groupId) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("GROUP_ID", groupId)
            .addValue("ROLE", USER.name());

        template.update(SQL_RESET_ROLE, sqlParameters);
    }

    private RowMapper<User> getResultDtoRowMapper() {
        return (resultSet, i) -> toDto(resultSet);
    }

    private User toDto(ResultSet resultSet) throws SQLException {
        return new User()
            .setId(resultSet.getInt("id"))
            .setName(resultSet.getString("name"))
            .setRole(UserRole.valueOf(resultSet.getString("role")));
    }
}
