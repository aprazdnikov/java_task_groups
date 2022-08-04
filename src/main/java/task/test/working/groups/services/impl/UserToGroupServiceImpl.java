package task.test.working.groups.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.test.working.groups.clients.api.KafkaClient;
import task.test.working.groups.data.CreateUserToGroupRequest;
import task.test.working.groups.data.Message;
import task.test.working.groups.data.User;
import task.test.working.groups.data.UserToGroup;
import task.test.working.groups.repositories.api.UserRepository;
import task.test.working.groups.repositories.api.UserToGroupRepository;
import task.test.working.groups.services.api.UserToGroupService;

import java.util.List;

import static task.test.working.groups.data.enums.UserRole.OWNER_GROUP;

@Service
@RequiredArgsConstructor
public class UserToGroupServiceImpl implements UserToGroupService {
    private final UserToGroupRepository userToGroupRepository;
    private final UserRepository userRepository;
    private final KafkaClient kafkaClient;

    @Override
    @Transactional
    public void createUserToGroup(CreateUserToGroupRequest createUserToGroup) {
        userRepository.updateUser(getUser(createUserToGroup));
        userToGroupRepository.setUserToGroup(getUserToGroup(createUserToGroup));
        if (OWNER_GROUP.equals(createUserToGroup.getRole())) {
            List<UserToGroup> userToGroups = userToGroupRepository.getUserToGroupByUserId(createUserToGroup.getUserId());

            userToGroups.stream()
                .map(UserToGroup::getGroupId)
                .forEach(userRepository::resetUserRoleByGroupId);

            userToGroups.stream()
                .map(UserToGroup::getGroupId)
                .forEach(groupId -> updateTasks(groupId, createUserToGroup.getUserId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserToGroup> getUserToGroupByUserId(Integer userId) {
        return userToGroupRepository.getUserToGroupByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserToGroup> getUserToGroupByGroupId(Integer groupId) {
        return userToGroupRepository.getUserToGroupByGroupId(groupId);
    }

    @Override
    @Transactional
    public void deleteUserToGroup(UserToGroup userToGroup) {
        userToGroupRepository.deleteUserToGroup(userToGroup);
        userRepository.getUserOwner(userToGroup.getGroupId())
            .ifPresent(user -> kafkaClient.send(getMessage(userToGroup.getUserId(), user.getId())));
    }

    private User getUser(CreateUserToGroupRequest createUserToGroupRequest) {
        return new User()
            .setId(createUserToGroupRequest.getUserId())
            .setName(createUserToGroupRequest.getUserName())
            .setRole(createUserToGroupRequest.getRole());
    }

    private UserToGroup getUserToGroup(CreateUserToGroupRequest createUserToGroupRequest) {
        return new UserToGroup()
            .setUserId(createUserToGroupRequest.getUserId())
            .setGroupId(createUserToGroupRequest.getGroupId());
    }

    private void updateTasks(Integer groupId, Integer ownerId) {
        userToGroupRepository.getUserToGroupByGroupId(groupId)
            .stream()
            .map(UserToGroup::getUserId)
            .forEach(userId -> kafkaClient.send(getMessage(userId, ownerId)));
    }

    private Message getMessage(Integer fromUser, Integer toUser) {
        return new Message()
            .setToUser(toUser)
            .setFromUser(fromUser);
    }
}
