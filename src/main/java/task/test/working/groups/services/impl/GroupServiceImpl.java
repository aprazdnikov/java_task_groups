package task.test.working.groups.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.test.working.groups.data.Group;
import task.test.working.groups.exceptions.BadRequestException;
import task.test.working.groups.exceptions.NotFoundException;
import task.test.working.groups.repositories.api.GroupRepository;
import task.test.working.groups.services.api.GroupService;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    public static final String FAILED_TO_CREATE_GROUP_WITH_PROJECT_ID = "failed to create group %s with project ID %s";
    public static final String GROUP_WITH_ID_S_NOT_FOUND = "Group with ID %s not found";
    public static final String NOT_FOUND_GROUP_FOR_UPDATE = "Not found group for update";
    public static final String NOT_FOUND_GROUP_FOR_DELETE = "Not found group for delete";

    private final GroupRepository repository;

    @Override
    @Transactional
    public Group createGroup(String name, Integer projectId) {
        return repository.createGroup(name, projectId)
            .stream()
            .findAny()
            .orElseThrow(() -> new BadRequestException(format(FAILED_TO_CREATE_GROUP_WITH_PROJECT_ID, name, projectId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Group getGroup(Integer id) {
        return repository.getGroup(id)
            .stream()
            .findAny()
            .orElseThrow(() -> new NotFoundException(format(GROUP_WITH_ID_S_NOT_FOUND, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> getGroupList(int limit, int offset) {
        return repository.getGroupList(limit, offset);
    }

    @Override
    @Transactional
    public void updateGroup(Group group) {
        if (!isFoundGroup(group.getId())) {
            throw new BadRequestException(NOT_FOUND_GROUP_FOR_UPDATE);
        }
        repository.updateGroup(group);
    }

    @Override
    @Transactional
    public void deleteGroup(Integer id) {
        if (!isFoundGroup(id)) {
            throw new BadRequestException(NOT_FOUND_GROUP_FOR_DELETE);
        }
        repository.deleteGroup(id);
    }

    private Boolean isFoundGroup(Integer id) {
        return repository.getGroup(id).isPresent();
    }
}
