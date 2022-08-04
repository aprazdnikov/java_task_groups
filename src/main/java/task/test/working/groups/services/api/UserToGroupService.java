package task.test.working.groups.services.api;

import task.test.working.groups.data.CreateUserToGroupRequest;
import task.test.working.groups.data.UserToGroup;

import java.util.List;

public interface UserToGroupService {
    /**
     * Создание связи пользователя с группой
     */
    void createUserToGroup(CreateUserToGroupRequest createUserToGroup);

    /**
     * Получение списка связей пользователя с группами
     */
    List<UserToGroup> getUserToGroupByUserId(Integer userId);
    List<UserToGroup> getUserToGroupByGroupId(Integer groupId);

    /**
     * Удаление связи пользователя с группой
     */
    void deleteUserToGroup(UserToGroup userToGroup);
}
