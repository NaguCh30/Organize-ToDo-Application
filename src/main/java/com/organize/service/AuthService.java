package com.organize.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.organize.dto.request.LoginRequest;
import com.organize.dto.request.RegisterRequest;
import com.organize.dto.response.AuthResponse;
import com.organize.dto.response.UserResponse;
import com.organize.entity.Role;
import com.organize.entity.User;
import com.organize.exception.EmailAlreadyExistsException;
import com.organize.exception.InvalidCredentialsException;
import com.organize.exception.PasswordMismatchException;
import com.organize.repository.UserRepository;
import com.organize.security.JwtService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class AuthService {
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    public AuthResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords not matching");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        
        LocalDateTime now = LocalDateTime.now();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getId());

        UserResponse userResponse = new UserResponse(savedUser.getId(), 
                                        savedUser.getUsername(), 
                                        savedUser.getEmail());
        
        return new AuthResponse(token, userResponse);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                            .orElseThrow(
                                () -> new InvalidCredentialsException("Invalid credentials")
                            );
        
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId());

        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail());

        return new AuthResponse(token, userResponse);
    }
}
