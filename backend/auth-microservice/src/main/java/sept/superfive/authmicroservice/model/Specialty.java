package sept.superfive.authmicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<Doctor> doctors;

    public Specialty(String specialtyName) {
        this.specialtyName = specialtyName;
    }
}
