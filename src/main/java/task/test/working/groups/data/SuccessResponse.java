package task.test.working.groups.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SuccessResponse {
    private final String result = "success";
}
