package com.sept.authmicroservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank // allow mononyms
    @Length(max = 25, message = "must not be not blank and length should be less than or equal to 25")
    private String firstName;

    @Length(max = 25, message = "Length should be less than or equal to 25")
    private String lastName;

    @NotBlank
    @Length(max = 254, message = "must not be blank and length should be less than or equal to 255")
    private String email;

    @NotBlank
    @Length(max = 45, min = 8, message = "must not be blank and length should be between 8 to 45")
    private String password;

    @NotNull
    @Length(min = 10, max = 10, message = "must be a valid date in the form - yyyy-MM-dd")
    private String dateOfBirth;
}
