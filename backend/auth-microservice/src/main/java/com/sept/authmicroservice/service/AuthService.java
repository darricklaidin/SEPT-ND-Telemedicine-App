package com.sept.authmicroservice.service;

import com.sept.authmicroservice.model.User;
import com.sept.authmicroservice.payload.JwtAuthenticationResponse;
import com.sept.authmicroservice.payload.LoginRequest;
import com.sept.authmicroservice.payload.SignUpRequest;
import com.sept.authmicroservice.repository.UserRepository;
import com.sept.authmicroservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername().trim(),
                        loginRequest.getPassword().trim()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new JwtAuthenticationResponse(true, jwt, "User");
    }

    public User registerUser(SignUpRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User newUser = new User(-1, signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), encodedPassword, LocalDate.parse(signUpRequest.getDateOfBirth()));

        return userRepository.save(newUser);
    }
}
