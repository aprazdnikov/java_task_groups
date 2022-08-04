package task.test.working.groups.repositories.api;

import task.test.working.groups.data.Group;
import task.test.working.groups.data.User;
import task.test.working.groups.data.UserToGroup;
import task.test.working.groups.data.enums.UserRole;

import java.util.Optional;

public interface UserRepository {
    /**
     * Получение пользователя
     */
    Optional<User> getUser(Integer id);

    /**
     * Получить руководителя группы
     */
    Optional<User> getUserOwner(Integer groupId);

    /**
     * Обновление пользователя
     */
    void updateUser(User user);

    /**
     * Удаление пользователя
     */
    void deleteUser(Integer id);

    /**
     * Установка роли пользователя всем находящимся в группе
     */
    void resetUserRoleByGroupId(Integer groupId);
}
