package task.test.working.groups.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task.test.working.groups.data.UserToGroup;
import task.test.working.groups.repositories.api.UserToGroupRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserToGroupRepositoryImpl implements UserToGroupRepository {
    private final NamedParameterJdbcTemplate template;

    private static final String SQL_INSERT =
        "INSERT INTO user_to_group (user_id, group_id) VALUES (:USER_ID, :GROUP_ID)" +
            "ON CONFLICT (user_id, group_id) DO NOTHING";
    private static final String SQL_QUERY_BY_USER_ID =
        "SELECT user_id, group_id FROM user_to_group WHERE user_id = :USER_ID";
    private static final String SQL_QUERY_BY_GROUP_ID =
        "SELECT user_id, group_id FROM user_to_group WHERE group_id = :GROUP_ID";
    private static final String SQL_DELETE =
        "DELETE FROM user_to_group WHERE user_id = :USER_ID AND group_id = :GROUP_ID";

    @Override
    public void setUserToGroup(UserToGroup userToGroup) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("USER_ID", userToGroup.getUserId())
            .addValue("GROUP_ID", userToGroup.getGroupId());

        template.update(SQL_INSERT, sqlParameters);
    }

    @Override
    public List<UserToGroup> getUserToGroupByUserId(Integer userId) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("USER_ID", userId);

        return template.query(SQL_QUERY_BY_USER_ID, sqlParameters, getResultDtoRowMapper());
    }

    @Override
    public List<UserToGroup> getUserToGroupByGroupId(Integer groupId) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("GROUP_ID", groupId);

        return template.query(SQL_QUERY_BY_GROUP_ID, sqlParameters, getResultDtoRowMapper());
    }

    @Override
    public void deleteUserToGroup(UserToGroup userToGroup) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("USER_ID", userToGroup.getUserId())
            .addValue("GROUP_ID", userToGroup.getGroupId());

        template.update(SQL_DELETE, sqlParameters);
    }

    private RowMapper<UserToGroup> getResultDtoRowMapper() {
        return (resultSet, i) -> toDto(resultSet);
    }

    private UserToGroup toDto(ResultSet resultSet) throws SQLException {
        return new UserToGroup()
            .setUserId(resultSet.getInt("user_id"))
            .setGroupId(resultSet.getInt("group_id"));
    }
}
