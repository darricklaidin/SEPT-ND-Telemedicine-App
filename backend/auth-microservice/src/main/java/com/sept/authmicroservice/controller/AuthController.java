package com.sept.authmicroservice.controller;

import javax.validation.Valid;

import com.sept.authmicroservice.model.User;
import com.sept.authmicroservice.payload.ApiResponse;
import com.sept.authmicroservice.payload.LoginRequest;
import com.sept.authmicroservice.payload.SignUpRequest;
import com.sept.authmicroservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;

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
            e.printStackTrace();
            body = new ApiResponse(false, e.getMessage());
        }

        return ResponseEntity.status(retStatus).body(body);
    }

    // register a new user
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        HttpStatus retStatus = HttpStatus.OK;
        Object body;
        try {
            User user = authService.registerUser(signUpRequest);
            body = new ApiResponse(true, "Successfully created user", user);
        } catch (DateTimeParseException e) {
            retStatus = HttpStatus.BAD_REQUEST;
            body = new ApiResponse(false, "Invalid value for DOB");
        } catch (Exception e) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
            body = new ApiResponse(false, e.getMessage());
        }

        return ResponseEntity.status(retStatus).body(body);
    }
}
