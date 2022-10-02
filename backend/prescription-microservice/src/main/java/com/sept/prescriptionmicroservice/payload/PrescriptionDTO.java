package com.sept.prescriptionmicroservice.payload;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PrescriptionDTO {

    private int prescriptionID;
    private int doctorID;
    private int patientID;
    private String prescription;

    public PrescriptionDTO(int prescriptionID, int doctorID, int patientID, String prescription) {
        this.prescriptionID = prescriptionID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.prescription = prescription;
    }
}
