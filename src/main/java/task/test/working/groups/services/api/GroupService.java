package task.test.working.groups.services.api;

import task.test.working.groups.data.Group;

import java.util.List;

public interface GroupService {
    /**
     * Создание группы
     */
    Group createGroup(String name, Integer projectId);

    /**
     * Получение списка групп
     */
    Group getGroup(Integer id);

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
