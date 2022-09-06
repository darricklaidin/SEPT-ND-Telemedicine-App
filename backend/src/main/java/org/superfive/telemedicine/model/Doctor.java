package org.superfive.telemedicine.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

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
    // Mapped bi-directionally to Appointment
    @OneToMany(
            mappedBy = "doctor",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<Appointment> appointments;

    public Doctor(int doctorID, String firstName, String lastName, String email, String password, Specialty specialty,
                  String accountStatus, Set<Appointment> appointments) {
        super(doctorID, firstName, lastName, email, password);
        this.specialty = specialty;
        this.accountStatus = accountStatus;
        this.appointments = appointments;
    }
    public Doctor() {
        super();
    }
}