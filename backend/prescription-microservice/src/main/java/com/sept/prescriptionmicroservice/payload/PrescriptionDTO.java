package com.sept.prescriptionmicroservice.payload;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PrescriptionDTO {

    private String prescription;

    public PrescriptionDTO( String prescription) {
        this.prescription = prescription;
    }
}
