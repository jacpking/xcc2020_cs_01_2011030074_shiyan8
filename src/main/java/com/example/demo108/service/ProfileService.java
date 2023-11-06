package com.example.demo108.service;

import com.example.demo108.detail.AuthUserDetails;
import com.example.demo108.dto.ProfileDto;

public interface ProfileService {
    ProfileDto getProfile(final String username, final AuthUserDetails authUserDetails);
    ProfileDto getProfileByUserId(Long userId, AuthUserDetails authUserDetails);

    ProfileDto followUser(String name, AuthUserDetails authUserDetails);

    ProfileDto unfollowUser(String name, AuthUserDetails authUserDetails);
}