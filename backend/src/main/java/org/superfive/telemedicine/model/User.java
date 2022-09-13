package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @NotBlank // allow mononyms
    @Column(length = 25)
    private String firstName;

    @Column(length = 25)
    private String lastName;

    @NotBlank
    @Column(length = 254)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Column(nullable = false, length = 60)
    private String password;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    public User() {
    }

    public User(int userID, String firstName, String lastName, String email, String password, LocalDate dateOfBirth) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }
}
