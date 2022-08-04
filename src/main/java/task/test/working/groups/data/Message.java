package task.test.working.groups.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    private Integer fromUser;
    private Integer toUser;
}
