package task.test.working.groups.data;

import lombok.Data;
import lombok.experimental.Accessors;
import task.test.working.groups.data.enums.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class User {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private UserRole role;
}
