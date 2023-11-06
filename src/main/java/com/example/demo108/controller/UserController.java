package com.example.demo108.controller;

import com.example.demo108.detail.AuthUserDetails;
import com.example.demo108.dto.UserDto;
import com.example.demo108.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto currentUser(
            @AuthenticationPrincipal AuthUserDetails authUserDetails
    ){
        return userService.currentUser(authUserDetails);
    }

    @PutMapping
    public UserDto update(
            @Valid @RequestBody UserDto.Update update,
            @AuthenticationPrincipal AuthUserDetails authUserDetails
    ){        return userService.update(update, authUserDetails);
    }
}
