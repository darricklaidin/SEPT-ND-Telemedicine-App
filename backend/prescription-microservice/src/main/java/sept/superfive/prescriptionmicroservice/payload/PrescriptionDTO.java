package sept.superfive.prescriptionmicroservice.payload;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDTO {

    private String prescription;

    @NotNull
    private int patientID;

    @NotNull
    private int doctorID;

    public PrescriptionDTO(int doctorID, int patientID, String prescription) {
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.prescription = prescription;
    }

    public PrescriptionDTO() {

    }
}
