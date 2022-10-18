package sept.superfive.authmicroservice.payload;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    @Length(max = 254)
    private String username;

    @NotBlank
    @Length(max = 45)
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
