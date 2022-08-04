package task.test.working.groups.repositories.api;

import task.test.working.groups.data.UserToGroup;

import java.util.List;

public interface UserToGroupRepository {
    /**
     * Создание привязки пользователя к группе
     */
    void setUserToGroup(UserToGroup userToGroup);

    /**
     * Получение привязки пользователя к группе
     */
    List<UserToGroup> getUserToGroupByUserId(Integer userId);
    List<UserToGroup> getUserToGroupByGroupId(Integer groupId);

    /**
     * Удаление привязки пользователя к группе
     */
    void deleteUserToGroup(UserToGroup userToGroup);
}
