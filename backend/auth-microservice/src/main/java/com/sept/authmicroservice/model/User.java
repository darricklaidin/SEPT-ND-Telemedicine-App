package com.sept.authmicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @NotBlank // allow mononyms
    @Column(length = 25)
    private String firstName;

    @Column(length = 25)
    private String lastName;

    @NotBlank
    @Column(unique = true, length = 254)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Column(nullable = false, length = 60)
    private String password;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Role> roles;

    public User(int userID, List<Role> roles) {
        this.userID = userID;
        this.roles = roles;
    }

    /*
     * UserDetails interface methods
     */
    @Override
    @JsonIgnore
    public Collection<Role> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
