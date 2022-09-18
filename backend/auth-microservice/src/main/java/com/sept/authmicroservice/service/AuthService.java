package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.*;
import com.sept.authmicroservice.payload.DoctorSignUp;
import com.sept.authmicroservice.payload.JwtAuthenticationResponse;
import com.sept.authmicroservice.payload.LoginRequest;
import com.sept.authmicroservice.payload.SignUpRequest;
import com.sept.authmicroservice.repository.RoleRepository;
import com.sept.authmicroservice.repository.SpecialtyRepository;
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
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SpecialtyRepository specialtyRepository;

    @Autowired
    public AuthService(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                       UserRepository userRepository, RoleRepository roleRepository, SpecialtyRepository specialtyRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.specialtyRepository = specialtyRepository;
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername().trim(),
                        loginRequest.getPassword().trim()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new JwtAuthenticationResponse(true, jwt, authentication.getAuthorities());
    }

    public User registerUser(SignUpRequest signUpRequest) {
        checkIfEmailAlreadyExists(signUpRequest.getEmail());

        // set roles
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName(RoleName.PATIENT));

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User newUser = new User(-1, signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), encodedPassword, parseDOB(signUpRequest.getDateOfBirth()), roles);
        return userRepository.save(newUser);
    }

    public Doctor registerDoctor(DoctorSignUp signUpRequest) {
        checkIfEmailAlreadyExists(signUpRequest.getEmail());

        // get specialty if exists
        Specialty specialty = specialtyRepository.findBySpecialtyID(signUpRequest.getSpecialtyId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialty", "specialtyID", signUpRequest.getSpecialtyId()));

        // set roles
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName(RoleName.DOCTOR));

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Doctor newUser = new Doctor(-1, signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(),
                encodedPassword, parseDOB(signUpRequest.getDateOfBirth()), roles, specialty);

        return userRepository.save(newUser);
    }

    private void checkIfEmailAlreadyExists(String email) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            throw new ResourceAlreadyExistsException("User", "email", email);
        }
    }

    private LocalDate parseDOB(String dob) {
        try {
            return LocalDate.parse(dob);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid value for DOB");
        }
    }
}
