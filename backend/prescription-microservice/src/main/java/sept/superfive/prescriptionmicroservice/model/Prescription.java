package sept.superfive.prescriptionmicroservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
