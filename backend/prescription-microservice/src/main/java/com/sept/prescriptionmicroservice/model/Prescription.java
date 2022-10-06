package com.sept.prescriptionmicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prescriptionID;

    private String prescription;

    @NotNull
    private int patientID;

    @NotNull
    private int doctorID;

    public Prescription() {}


    public Prescription(int prescriptionID, int doctorID, int patientID, String prescription) {
        this.prescriptionID = prescriptionID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.prescription = prescription;
    }
}
