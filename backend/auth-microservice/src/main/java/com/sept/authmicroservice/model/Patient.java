package com.sept.authmicroservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient extends User {
    // TODO: Prescribed medicines
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientID;

    @NotNull
    @Column(unique = true)
    private int userID;

    public Patient(int patientID, int userID) {
        this.patientID = patientID;
        this.userID = userID;
    }
}
