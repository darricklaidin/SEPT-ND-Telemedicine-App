package com.sept.authmicroservice.unit_test;

import com.sept.authmicroservice.repository.UserRepository;
import com.sept.authmicroservice.repository.PatientRepository;
import com.sept.authmicroservice.service.PatientService;
import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.service.DoctorService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.BDDMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import com.sept.authmicroservice.model.*;
import com.sept.authmicroservice.service.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceUnitTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void authenticateUser() {
    }

    @Test
    void registerPatient() {
    }

    @Test
    void registerDoctor() {
    }

    @Test
    void getUser() {
    }
}