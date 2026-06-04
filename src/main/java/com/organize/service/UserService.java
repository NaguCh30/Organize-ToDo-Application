package com.organize.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.organize.dto.response.UserResponse;
import com.organize.entity.User;
import com.organize.repository.UserRepository;
import com.organize.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public UserResponse getCurrentUser() {

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
