package task.test.working.groups.repositories.api;

import task.test.working.groups.data.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    /**
     * Создание группы
     */
    Optional<Group> createGroup(String name, Integer projectId);

    /**
     * Получение списка групп
     */
    Optional<Group> getGroup(Integer id);

    /**
     * Получение списка групп
     */
    List<Group> getGroupList(int limit, int offset);

    /**
     * Обновление группы
     */
    void updateGroup(Group group);

    /**
     * Удаление группы
     */
    void deleteGroup(Integer id);
}
