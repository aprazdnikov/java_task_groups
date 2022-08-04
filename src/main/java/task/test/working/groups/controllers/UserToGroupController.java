package task.test.working.groups.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.test.working.groups.data.CreateUserToGroupRequest;
import task.test.working.groups.data.SuccessResponse;
import task.test.working.groups.data.UserToGroup;
import task.test.working.groups.services.api.UserToGroupService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@Validated
@RestController
@RequestMapping("/user-to-group/")
@RequiredArgsConstructor
public class UserToGroupController {
    private final UserToGroupService service;
    private final SuccessResponse successResponse = new SuccessResponse();

    @PostMapping
    @ApiOperation(value = "Create user to group")
    public HttpEntity<SuccessResponse> createGroup(@Valid @RequestBody CreateUserToGroupRequest request) {
        service.createUserToGroup(request);

        return status(CREATED).body(successResponse);
    }

    @GetMapping(value = "by-user/{id}")
    @ApiOperation(value = "Get user's groups")
    public List<UserToGroup> getUserToGroupByUserId(@PathVariable Integer id) {
        return service.getUserToGroupByUserId(id);
    }

    @GetMapping(value = "by-group/{id}")
    @ApiOperation(value = "Get user's groups")
    public List<UserToGroup> getUserToGroupByGroupId(@PathVariable Integer id) {
        return service.getUserToGroupByGroupId(id);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete user to group")
    public HttpEntity<Object> deleteUserToGroup(@RequestBody UserToGroup userToGroup) {
        service.deleteUserToGroup(userToGroup);

        return status(NO_CONTENT).build();
    }
}