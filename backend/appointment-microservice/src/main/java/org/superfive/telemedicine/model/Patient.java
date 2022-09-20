package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    // TODO: Prescribed medicines
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientID;

    @NotNull
    @Column(unique = true)
    private int userID;

    // Appointments
    @JsonIgnore
    @OneToMany(
            mappedBy = "patient",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Appointment> appointments;

    public Patient(int patientID, int userID) {
        this.patientID = patientID;
        this.userID = userID;
    }
}
