package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.*;
import com.sept.authmicroservice.payload.*;
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
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

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
        // get user id
        User user = (User) authentication.getPrincipal();
        int userId = user.getUserID();

        // generate token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new JwtAuthenticationResponse(true, jwt, userId, authentication.getAuthorities());
    }

    public User registerUser(SignUpRequest signUpRequest) {
        checkIfEmailAlreadyExists(signUpRequest.getEmail());

        // set roles
        List<Role> roles = new ArrayList<>();
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
        List<Role> roles = new ArrayList<>();
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

    public UserDTO getUser(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return tokenProvider.getUserDtoFromToken(jwt);
        } else {
            throw new IllegalArgumentException("Invalid token format");
        }
    }
}
