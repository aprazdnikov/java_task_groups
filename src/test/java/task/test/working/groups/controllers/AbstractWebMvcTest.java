package task.test.working.groups.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import task.test.working.groups.AbstractTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class AbstractWebMvcTest extends AbstractTest {
    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @Before
    public final void before() {
        if (this.mockMvc == null) {
            mockMvc = webAppContextSetup(webApplicationContext)
                    .alwaysDo(print())
                    .build();
        }
    }

    protected String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
