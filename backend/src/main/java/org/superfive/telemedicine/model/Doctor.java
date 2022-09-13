package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

@Entity
@AttributeOverride(name = "userID", column = @Column(name = "doctorID"))
@Getter
@Setter
public class Doctor extends User {
    // Doctor's specialty
    @ManyToOne
    @JoinColumn(name = "specialtyID", referencedColumnName = "specialtyID", nullable = false)
    private Specialty specialty;

    // Controls whether account is ACTIVE or DEACTIVATED
    @NotBlank
    private String accountStatus;

    // Doctor's appointments
    // Mapped bi-directionally to Appointment
    @JsonIgnore
    @OneToMany(
            mappedBy = "doctor",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Appointment> appointments;

    // Doctor's availability
    @JsonIgnore
    @OneToMany(
            mappedBy = "doctor",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Availability> availabilities;

    public Doctor(int doctorID, String firstName, String lastName, String email, String password, LocalDate dateOfBirth,
                  Specialty specialty, String accountStatus, Set<Availability> availabilities, Set<Appointment> appointments) {
        super(doctorID, firstName, lastName, email, password, dateOfBirth);
        this.specialty = specialty;
        this.accountStatus = accountStatus;
        this.availabilities = availabilities;
        this.appointments = appointments;
    }

    public Doctor() {
        super();
    }
}