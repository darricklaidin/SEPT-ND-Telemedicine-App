package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int specialtyID;

    @Column(length = 50)
    @NotBlank
    private String name;

    @JsonIgnore
    @OneToMany(
            mappedBy = "specialty",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<Doctor> doctors;
}