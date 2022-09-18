package com.sept.authmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends User {
    @ManyToOne
    @JoinColumn(name = "specialtyID", referencedColumnName = "specialtyID", nullable = false)
    private Specialty specialty;
}
