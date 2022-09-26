package com.sept.authmicroservice.controller;

import javax.validation.Valid;

import com.sept.authmicroservice.model.User;
import com.sept.authmicroservice.payload.ApiResponse;
import com.sept.authmicroservice.payload.DoctorSignUp;
import com.sept.authmicroservice.payload.LoginRequest;
import com.sept.authmicroservice.payload.SignUpRequest;
import com.sept.authmicroservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // login user
    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        HttpStatus retStatus = HttpStatus.OK;
        Object body;
        try {
            body = authService.authenticateUser(loginRequest);
        } catch (DisabledException | LockedException e) {
            retStatus = HttpStatus.FORBIDDEN;
            body = new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            retStatus = HttpStatus.UNAUTHORIZED;
            body = new ApiResponse(false, "Invalid Credentials");
        }

        return ResponseEntity.status(retStatus).body(body);
    }

    // register a new patient
    @PostMapping("/signup")
    public ResponseEntity<User> registerPatient(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = authService.registerPatient(signUpRequest);
        return ResponseEntity.ok().body(user);
    }

    // register a new doctor
    @PostMapping("/signup-doctor")
    public ResponseEntity<User> registerDoctor(@Valid @RequestBody DoctorSignUp signUpRequest) {
        User user = authService.registerDoctor(signUpRequest);
        return ResponseEntity.ok().body(user);
    }

    // validate token
    @GetMapping("/validate")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String token) {
        HttpStatus retStatus = HttpStatus.OK;
        Object body;
        try {
            body = authService.getUser(token);
        } catch (IllegalArgumentException e) {
            retStatus = HttpStatus.BAD_REQUEST;
            body = new ApiResponse(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retStatus = HttpStatus.UNAUTHORIZED;
            body = new ApiResponse(false, "Validation failed");
        }

        return ResponseEntity.status(retStatus).body(body);
    }
}
