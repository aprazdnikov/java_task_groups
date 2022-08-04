package task.test.working.groups;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import task.test.working.groups.embedded.EmbeddedPostgresql;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
@Transactional
public abstract class AbstractTest {
    @ClassRule
    public static EmbeddedPostgresql postgres = EmbeddedPostgresql.getInstance();
}
