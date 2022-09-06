package org.superfive.telemedicine.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@AttributeOverride(name = "userID", column = @Column(name = "patientID"))
@Getter
public class Patient extends User {
    // TODO: Prescribed medicines


    private String accountStatus;  // ACTIVE or DEACTIVATED

    // Appointments
    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
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
