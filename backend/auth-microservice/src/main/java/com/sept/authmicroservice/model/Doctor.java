package com.sept.authmicroservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends User {
    @ManyToOne()
    @JoinColumn(name = "specialtyID", referencedColumnName = "specialtyID", nullable = false)
    private Specialty specialty;

    public Doctor(int userID, String firstName, String lastName, String email, String password,
                  LocalDate dateOfBirth, List<Role> roles, Specialty specialty) {
        super(userID, firstName, lastName, email, password, dateOfBirth, roles);
        this.specialty = specialty;
    }
}
