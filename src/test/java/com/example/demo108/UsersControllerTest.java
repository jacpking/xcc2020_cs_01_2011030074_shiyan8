package com.example.demo108;


import com.example.demo108.controller.UsersController;
import com.example.demo108.dto.UserDto;
import com.example.demo108.ex.AppException;
import com.example.demo108.filter.JWTAuthFilter;
import com.example.demo108.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.demo108.ex.Error;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    JWTAuthFilter jwtAuthFilter;

    @MockBean
    AuthenticationManager authenticationManager;

    @MethodSource("validUserRegistration")
    @ParameterizedTest
    void whenValidRegisterInfo_thenReturnUser(UserDto.Registration registration) throws Exception {
        UserDto result = UserDto.builder().email(registration.getEmail()).username(registration.getUsername()).build();
        when(userService.registration(any(UserDto.Registration.class))).thenReturn(result);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(registration.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(registration.getUsername())));
    }

    @MethodSource("invalidUserRegistration")
    @ParameterizedTest
    void whenInvalidRegistrationInfo_then422(UserDto.Registration registration) throws Exception{
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @MethodSource("validUserRegistration")
    @ParameterizedTest
    void whenDuplicatedRegisterInfo_thenThrow422(UserDto.Registration registration) throws Exception {
        when(userService.registration(any(UserDto.Registration.class))).thenThrow(new AppException(Error.DUPLICATED_USER));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.body[0]", Matchers.is(Error.DUPLICATED_USER.getMessage())));
    }

    @Test
    void whenValidLoginInfo_thenReturnUser() throws Exception {
        UserDto.Login login = UserDto.Login.builder().email("test@test.com").password("password123").build();
        UserDto result = UserDto.builder().email(login.getEmail()).username("testman").token("token.test.needed").build();

        when(userService.login(any(UserDto.Login.class))).thenReturn(result);

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(login.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.token", Matchers.notNullValue()));
    }

    @Test
    void whenInvalidLoginInfo_thenReturn422() throws Exception {
        UserDto.Login login = UserDto.Login.builder().email("test@test.com").password("password123").build();

        when(userService.login(any(UserDto.Login.class))).thenThrow(new AppException(Error.LOGIN_INFO_INVALID));

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.body[0]", Matchers.is(Error.LOGIN_INFO_INVALID.getMessage())));
    }

    public static Stream<Arguments> validUserRegistration() {
        return Stream.of(
                Arguments.of(UserDto.Registration.builder().email("test@test.com").username("testman").password("password").build()),
                Arguments.of(UserDto.Registration.builder().email("test2@test.com").username("testman2").password("password").build()),
                Arguments.of(UserDto.Registration.builder().email("test@test.com").username("testman3").password("password").build())
        );
    }

    public static Stream<Arguments> invalidUserRegistration() {
        return Stream.of(
                Arguments.of(UserDto.Registration.builder().email("test.com").username("testman").password("password").build()),
                Arguments.of(UserDto.Registration.builder().email("test2@test.com").username("test man2").password("password").build()),
                Arguments.of(UserDto.Registration.builder().email("test").password("password").build()),
                Arguments.of(UserDto.Registration.builder().password("password").build())
        );
    }
}
