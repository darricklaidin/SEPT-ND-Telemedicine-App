package com.example.backend.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class Doctor extends User {
    @ManyToOne()
    @JoinColumn(nullable = false)
    private Specialty specialty;

    public Doctor(int id, String firstName, String lastName, String email, String password, Specialty specialty) {
        super(id, firstName, lastName, email, password);
        this.specialty = specialty;
    }

    public Doctor() {
        super();
    }
}