package task.test.working.groups.data;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CreateGroupRequest {
    @NotBlank
    private String name;
    private Integer projectId;
}
