package task.test.working.groups.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import task.test.working.groups.data.CreateUserToGroupRequest;
import task.test.working.groups.data.User;
import task.test.working.groups.data.UserToGroup;
import task.test.working.groups.repositories.api.UserRepository;
import task.test.working.groups.repositories.api.UserToGroupRepository;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static task.test.working.groups.data.enums.UserRole.OWNER_GROUP;
import static task.test.working.groups.data.enums.UserRole.USER;


public class UserToGroupControllerTest extends AbstractWebMvcTest {
    private static final String USER_TO_GROUP = "/user-to-group/";
    private static final String URL_BY_USER_ID = USER_TO_GROUP + "by-user/%s";
    private static final String URL_BY_GROUP_ID = USER_TO_GROUP + "by-group/%s";

    @Autowired
    private UserToGroupRepository userToGroupRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUserToGroup() throws Exception {
        User oldUser = new User()
            .setId(10)
            .setName("test")
            .setRole(OWNER_GROUP);
        userRepository.updateUser(oldUser);
        userToGroupRepository.setUserToGroup(new UserToGroup().setUserId(oldUser.getId()).setGroupId(1));

        assertThat(userToGroupRepository.getUserToGroupByUserId(1).size()).isEqualTo(0);
        assertThat(userRepository.getUser(oldUser.getId()).get().getRole()).isEqualTo(OWNER_GROUP);

        CreateUserToGroupRequest request = new CreateUserToGroupRequest()
            .setUserId(1)
            .setUserName("test")
            .setRole(OWNER_GROUP)
            .setGroupId(1);

        mockMvc.perform(post(USER_TO_GROUP)
            .contentType(APPLICATION_JSON)
            .content(toJson(request)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.result", is("success")))
        ;
        assertThat(userToGroupRepository.getUserToGroupByUserId(1).size()).isEqualTo(1);
        assertThat(userRepository.getUser(oldUser.getId()).get().getRole()).isEqualTo(USER);

        mockMvc.perform(post(USER_TO_GROUP)
                .contentType(APPLICATION_JSON)
                .content(toJson(request.setGroupId(2))))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.result", is("success")))
        ;
        assertThat(userToGroupRepository.getUserToGroupByUserId(1).size()).isEqualTo(2);
    }

    @Test
    public void testCreateUserToGroupBadRequest() throws Exception {
        mockMvc.perform(post(USER_TO_GROUP)
                .contentType(APPLICATION_JSON)
                .content(toJson(new CreateUserToGroupRequest())))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.role", is("must not be null")))
            .andExpect(jsonPath("$.userId", is("must not be null")))
            .andExpect(jsonPath("$.userName", is("must not be null")))
            .andExpect(jsonPath("$.groupId", is("must not be null")))
        ;
    }

    @Test
    public void testUserToGroupList() throws Exception {
        User user = new User().setId(1).setName("name").setRole(USER);
        UserToGroup userToGroup = new UserToGroup().setUserId(user.getId()).setGroupId(1);

        userRepository.updateUser(user);
        userToGroupRepository.setUserToGroup(userToGroup);
        userToGroupRepository.setUserToGroup(userToGroup.setGroupId(2));

        mockMvc.perform(get(format(URL_BY_USER_ID, 1))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
        ;

        mockMvc.perform(get(format(URL_BY_USER_ID, 2))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(0)))
        ;

        mockMvc.perform(get(format(URL_BY_GROUP_ID, 1))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(1)))
        ;

        mockMvc.perform(get(format(URL_BY_GROUP_ID, 2))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(1)))
        ;
    }

    @Test
    public void testDeleteGroup() throws Exception {
        User user = new User().setId(1).setName("name").setRole(USER);
        UserToGroup userToGroup = new UserToGroup().setUserId(user.getId()).setGroupId(1);

        userRepository.updateUser(user);
        userToGroupRepository.setUserToGroup(userToGroup);
        assertThat(userToGroupRepository.getUserToGroupByUserId(userToGroup.getUserId()).size()).isEqualTo(1);

        mockMvc.perform(delete(USER_TO_GROUP)
                .contentType(APPLICATION_JSON)
                .content(toJson(userToGroup)))
            .andExpect(status().isNoContent())
        ;

        assertThat(userToGroupRepository.getUserToGroupByUserId(userToGroup.getUserId()).size()).isEqualTo(0);
        assertThat(userToGroupRepository.getUserToGroupByGroupId(userToGroup.getGroupId()).size()).isEqualTo(0);
    }
}
