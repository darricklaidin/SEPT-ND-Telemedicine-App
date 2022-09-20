package com.sept.authmicroservice.payload;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SpecialtyDTO {
    @NotBlank
    @Length(max = 12)
    private String specialtyName;
}
