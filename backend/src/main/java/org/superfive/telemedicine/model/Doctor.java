package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@AttributeOverride(name = "userID", column = @Column(name = "doctorID"))
@Getter
public class Doctor extends User {
    // Doctor's specialty
    @ManyToOne
    @JoinColumn(name = "specialtyID", referencedColumnName = "specialtyID", nullable = false)

    private Specialty specialty;

    // Controls whether account is ACTIVE or DEACTIVATED
    private String accountStatus;

    // Doctor's appointments
    @OneToMany(mappedBy = "doctor") // Mapped bi-directionally to Appointment
    @JsonIgnore
    private List<Appointment> appointments;

    public Doctor(int doctorID, String firstName, String lastName, String email, String password, Specialty specialty,
                  String accountStatus, List<Appointment> appointments) {
        super(doctorID, firstName, lastName, email, password);
        this.specialty = specialty;
        this.accountStatus = accountStatus;
        this.appointments = appointments;
    }
    public Doctor() {
        super();
    }
}