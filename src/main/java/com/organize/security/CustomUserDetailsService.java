package com.organize.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.organize.entity.User;
import com.organize.exception.UserNotFoundException;
import com.organize.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserById(String userId) {
        User user = userRepository.findById(userId)
                                    .orElseThrow(
                                        () -> new UserNotFoundException("User not Fund")
                                    );
        
        return new CustomUserDetails(user);
    }
}
