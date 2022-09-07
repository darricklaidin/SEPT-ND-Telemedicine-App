package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@AttributeOverride(name = "userID", column = @Column(name = "patientID"))
@Getter
@Setter
public class Patient extends User {
    // TODO: Prescribed medicines

    @NotBlank
    private String accountStatus;  // ACTIVE or DEACTIVATED

    // Appointments
    @JsonIgnore
    @OneToMany(
            mappedBy = "patient",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Appointment> appointments;

    public Patient() {
        super();
    }

    public Patient(int patientID, String firstName, String lastName, String email, String password,
                   String accountStatus, Set<Appointment> appointments) {
        super(patientID, firstName, lastName, email, password);
        this.accountStatus = accountStatus;
        this.appointments = appointments;
    }


}
