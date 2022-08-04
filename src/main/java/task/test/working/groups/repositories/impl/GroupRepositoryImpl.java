package task.test.working.groups.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import task.test.working.groups.data.Group;
import task.test.working.groups.repositories.api.GroupRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {
    private final NamedParameterJdbcTemplate template;

    private static final String SQL_INSERT =
        "INSERT INTO groups (name, project_id) VALUES (:NAME, :PROJECT_ID) RETURNING id, name, project_id";
    private static final String SQL_QUERY =
        "SELECT id, name, project_id FROM groups WHERE id = :ID";
    private static final String SQL_QUERY_LIST =
        "SELECT id, name, project_id FROM groups ORDER BY id LIMIT :LIMIT OFFSET :OFFSET";
    private static final String SQL_UPDATE =
        "UPDATE groups SET name = :NAME, project_id = :PROJECT_ID WHERE id = :ID";
    private static final String SQL_DELETE =
        "DELETE FROM groups WHERE id = :ID";

    @Override
    public Optional<Group> createGroup(String name, Integer projectId) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("NAME", name)
            .addValue("PROJECT_ID", projectId);

        return template.query(SQL_INSERT, sqlParameters, getResultDtoRowMapper())
            .stream()
            .findAny();
    }

    @Override
    public Optional<Group> getGroup(Integer id) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource().addValue("ID", id);

        return template.query(SQL_QUERY, sqlParameters, getResultDtoRowMapper())
            .stream()
            .findAny();
    }

    @Override
    public List<Group> getGroupList(int limit, int offset) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("LIMIT", limit)
            .addValue("OFFSET", offset);

        return template.query(SQL_QUERY_LIST, sqlParameters, getResultDtoRowMapper());
    }

    @Override
    public void updateGroup(Group group) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
            .addValue("ID", group.getId())
            .addValue("NAME", group.getName())
            .addValue("PROJECT_ID", group.getProjectId());

        template.update(SQL_UPDATE, sqlParameters);
    }

    @Override
    public void deleteGroup(Integer id) {
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource().addValue("ID", id);
        template.update(SQL_DELETE, sqlParameters);
    }

    private RowMapper<Group> getResultDtoRowMapper() {
        return (resultSet, i) -> toDto(resultSet);
    }

    private Group toDto(ResultSet resultSet) throws SQLException {
        Group group = new Group()
            .setId(resultSet.getInt("id"))
            .setName(resultSet.getString("name"));

        ofNullable(resultSet.getObject("project_id"))
            .ifPresent(projectId -> group.setProjectId((Integer) projectId));

        return group;
    }
}
