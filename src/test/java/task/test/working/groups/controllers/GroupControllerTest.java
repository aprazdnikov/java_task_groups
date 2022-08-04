package task.test.working.groups.controllers;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import task.test.working.groups.data.CreateGroupRequest;
import task.test.working.groups.data.Group;
import task.test.working.groups.repositories.api.GroupRepository;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class GroupControllerTest extends AbstractWebMvcTest {
    private static final String URL_GROUP = "/group/";
    private static final String URL_GROUP_ID = "/group/%s";

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void testCreateGroup() throws Exception {
        mockMvc.perform(post(URL_GROUP)
            .contentType(APPLICATION_JSON)
            .content(toJson(new CreateGroupRequest().setName("test"))))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id", IsNull.notNullValue()))
            .andExpect(jsonPath("$.name", is("test")))
            .andExpect(jsonPath("$.projectId", IsNull.nullValue()))
        ;
    }

    @Test
    public void testCreateGroupBadRequest() throws Exception {
        mockMvc.perform(post(URL_GROUP)
                .contentType(APPLICATION_JSON)
                .content(toJson(new CreateGroupRequest())))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.name", is("must not be blank")))
        ;
    }

    @Test
    public void testGroupList() throws Exception {
        mockMvc.perform(get(URL_GROUP)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
        ;
    }

    @Test
    public void testGroupById() throws Exception {
        mockMvc.perform(get(format(URL_GROUP_ID, 1))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("group1")))
        ;
    }

    @Test
    public void testGroupByIdNotFound() throws Exception {
        mockMvc.perform(get(format(URL_GROUP_ID, 100))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.error", is("Group with ID 100 not found")))
        ;
    }

    @Test
    public void testUpdateGroup() throws Exception {
        mockMvc.perform(patch(URL_GROUP)
                .contentType(APPLICATION_JSON)
                .content(toJson(new Group().setId(1).setName("test"))))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.result", is("success")))
        ;

        Group group = groupRepository.getGroup(1).get();
        assertThat(group.getName()).isEqualTo("test");
    }

    @Test
    public void testUpdateGroupBadRequest() throws Exception {
        mockMvc.perform(patch(URL_GROUP)
                .contentType(APPLICATION_JSON)
                .content(toJson(new Group().setId(100).setName("test"))))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.error", is("Not found group for update")))
        ;
    }

    @Test
    public void testDeleteGroup() throws Exception {
        assertThat(groupRepository.getGroupList(100, 0).size()).isEqualTo(2);

        mockMvc.perform(delete(format(URL_GROUP_ID, 1))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
        ;

        assertThat(groupRepository.getGroup(1).isPresent()).isFalse();
        assertThat(groupRepository.getGroupList(100, 0).size()).isEqualTo(1);
    }

    @Test
    public void testDeleteGroupBadRequest() throws Exception {
        mockMvc.perform(delete(format(URL_GROUP_ID, 100))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.error", is("Not found group for delete")))
        ;
    }
}
