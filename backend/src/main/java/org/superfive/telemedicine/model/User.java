package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Getter
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

    @JsonIgnore
    @NotBlank
    @Column(nullable = false, length = 60)
    private String password;

    public User() {
    }

    public User(int userID, String firstName, String lastName, String email, String password) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
