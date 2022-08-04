package task.test.working.groups.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.test.working.groups.data.CreateGroupRequest;
import task.test.working.groups.data.Group;
import task.test.working.groups.data.SuccessResponse;
import task.test.working.groups.services.api.GroupService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@Validated
@RestController
@RequestMapping("/group/")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;
    private final SuccessResponse successResponse = new SuccessResponse();

    @PostMapping
    @ApiOperation(value = "Create group")
    public HttpEntity<Group> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        Group group = service.createGroup(request.getName(), request.getProjectId());

        return status(CREATED).body(group);
    }

    @GetMapping(value = "{id}")
    @ApiOperation(value = "Get group by id")
    public Group getGroup(@PathVariable Integer id) {
        return service.getGroup(id);
    }

    @GetMapping
    @ApiOperation(value = "Get groups")
    public List<Group> getGroupList(@RequestParam(defaultValue = "100") Integer limit,
                                    @RequestParam(defaultValue = "0") Integer offset) {
       return service.getGroupList(limit, offset);
    }

    @PatchMapping
    @ApiOperation(value = "Update group")
    public SuccessResponse getWorkingGroupList(@RequestBody Group group) {
        service.updateGroup(group);

        return successResponse;
    }

    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "Delete group")
    public HttpEntity<Object> deleteGroup(@PathVariable Integer id) {
        service.deleteGroup(id);

        return status(NO_CONTENT).build();
    }
}