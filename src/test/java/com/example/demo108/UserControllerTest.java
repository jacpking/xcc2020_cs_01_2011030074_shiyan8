package com.example.demo108;

import com.example.demo108.config.WithAuthUser;
import com.example.demo108.controller.UserController;
import com.example.demo108.detail.AuthUserDetails;
import com.example.demo108.dto.UserDto;
import com.example.demo108.filter.JWTAuthFilter;
import com.example.demo108.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JWTAuthFilter jwtAuthFilter;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserService userService;

    @Test
    @WithAuthUser
    void whenAuthorizedUser_returnUserDto() throws Exception {
        UserDto result = UserDto.builder().email("email@email.com").username("username").build();

        when(userService.currentUser(any(AuthUserDetails.class))).thenReturn(result);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(result.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(result.getUsername())));
    }

    @Test
    @WithAuthUser
    void whenUpdateDto_returnUpdatedUserDto() throws Exception {
        UserDto.Update update = UserDto.Update.builder().username("newName").bio("newBio").build();
        UserDto result = UserDto.builder().username("newName").bio("newBio").build();

        when(userService.update(any(UserDto.Update.class), any(AuthUserDetails.class))).thenReturn(result);

        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(update.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.bio", Matchers.is(update.getBio())));
    }
}