package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int specialtyID;

    @Column(length = 50)
    @NotBlank
    private String specialtyName;

    @JsonIgnore
    @OneToMany(
            mappedBy = "specialty",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Doctor> doctors;

    public Specialty(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public Specialty() {
    }
}