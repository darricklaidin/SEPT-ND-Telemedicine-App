package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@AttributeOverride(name = "userID", column = @Column(name = "patientID"))
@Getter
public class Patient extends User {
    // TODO: Prescribed medicines


    private String accountStatus;  // ACTIVE or DEACTIVATED

    // Appointments
    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<Appointment> appointments;

    public Patient() {
        super();
    }

    public Patient(int patientID, String firstName, String lastName, String email, String password,
                   String accountStatus, List<Appointment> appointments) {
        super(patientID, firstName, lastName, email, password);
        this.accountStatus = accountStatus;
        this.appointments = appointments;
    }


}
