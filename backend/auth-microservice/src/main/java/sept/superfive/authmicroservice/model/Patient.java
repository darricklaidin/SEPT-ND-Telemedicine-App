package sept.superfive.authmicroservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient extends User {
    private String symptoms;

    public Patient(int userID, String firstName, String lastName, String email, String password,
                   LocalDate dateOfBirth, List<Role> roles) {
        super(userID, firstName, lastName, email, password, dateOfBirth, roles);
        this.symptoms = null;
    }
}
