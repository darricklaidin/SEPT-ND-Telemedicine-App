package com.sept.prescriptionmicroservice.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Getter
@Setter
public class PrescriptionDTO {
    private int prescriptionID;

    private String prescription;

    @NotNull
    private int patientID;

    @NotNull
    private int doctorID;

    public PrescriptionDTO(int prescriptionID, int doctorID, int patientID, String prescription) {
        this.prescriptionID = prescriptionID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.prescription = prescription;
    }

    public PrescriptionDTO() {

    }
}
