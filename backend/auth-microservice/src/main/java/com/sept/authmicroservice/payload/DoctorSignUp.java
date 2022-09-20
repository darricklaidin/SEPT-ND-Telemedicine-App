package com.sept.authmicroservice.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DoctorSignUp extends SignUpRequest {
    @NotNull
    private Integer specialtyId;

    public DoctorSignUp(String firstName, String lastName, String email, String password,
            String dateOfBirth, Integer specialtyId) {
        super(firstName, lastName, email, password, dateOfBirth);
        this.specialtyId = specialtyId;
    }
}
