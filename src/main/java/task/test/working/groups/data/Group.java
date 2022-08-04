package task.test.working.groups.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Group {
    private Integer id;
    private String name;
    private Integer projectId;
}
