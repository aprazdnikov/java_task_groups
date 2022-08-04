package task.test.working.groups.data;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UserToGroup {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer groupId;
}
