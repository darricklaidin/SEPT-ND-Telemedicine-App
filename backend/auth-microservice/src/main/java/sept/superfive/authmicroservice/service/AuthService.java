package sept.superfive.authmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sept.superfive.authmicroservice.exception.ResourceAlreadyExistsException;
import sept.superfive.authmicroservice.exception.ResourceNotFoundException;
import sept.superfive.authmicroservice.model.*;
import sept.superfive.authmicroservice.payload.*;
import sept.superfive.authmicroservice.repository.RoleRepository;
import sept.superfive.authmicroservice.repository.SpecialtyRepository;
import sept.superfive.authmicroservice.repository.UserRepository;
import sept.superfive.authmicroservice.security.JwtTokenProvider;

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

    public User registerPatient(SignUpRequest signUpRequest) {
        checkIfEmailAlreadyExists(signUpRequest.getEmail());

        // set roles
        List<Role> roles = new ArrayList<>();
        roles.add(
                roleRepository.findByName(RoleName.PATIENT));

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Patient newPatient = new Patient(-1, signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), encodedPassword, parseDOB(signUpRequest.getDateOfBirth()), roles);
        return userRepository.save(newPatient);
    }

    public Doctor registerDoctor(DoctorSignUp signUpRequest) {
        checkIfEmailAlreadyExists(signUpRequest.getEmail());

        // get specialty if exists
        Specialty specialty = specialtyRepository.findBySpecialtyID(signUpRequest.getSpecialtyID())
                .orElseThrow(() -> new ResourceNotFoundException("Specialty", "specialtyID", signUpRequest.getSpecialtyID()));

        // set roles
        List<Role> roles = new ArrayList<>();
        roles.add(
                roleRepository.findByName(RoleName.DOCTOR));

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Doctor newDoctor = new Doctor(-1, signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(),
                encodedPassword, parseDOB(signUpRequest.getDateOfBirth()), roles, specialty);

        return userRepository.save(newDoctor);
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

    /**
     * Gets a UserDTO from the JWT
     */
    public UserDTO getUser(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return tokenProvider.getUserDtoFromToken(jwt);
        } else {
            throw new IllegalArgumentException("Invalid token format");
        }
    }
}
