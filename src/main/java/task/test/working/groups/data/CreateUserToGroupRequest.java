package task.test.working.groups.data;

import lombok.Data;
import lombok.experimental.Accessors;
import task.test.working.groups.data.enums.UserRole;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CreateUserToGroupRequest {
    @NotNull
    private Integer userId;
    @NotNull
    private String userName;
    @NotNull
    private UserRole role;
    @NotNull
    private Integer groupId;
}
