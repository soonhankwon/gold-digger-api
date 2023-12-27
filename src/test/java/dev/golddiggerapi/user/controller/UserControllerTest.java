package dev.golddiggerapi.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(value = "ADMIN", roles = {"SUPER"})
    void createUser() throws Exception {
        UserSignupRequest request = new UserSignupRequest("betaTester", "password1!", false, "www");
        when(userService.createUser(request)).thenReturn("created");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "betaTester");
        jsonObject.put("password", "password1!");
        jsonObject.put("subscribeNotification", false);
        jsonObject.put("discordUrl", "www");

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .with(csrf().asHeader()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}