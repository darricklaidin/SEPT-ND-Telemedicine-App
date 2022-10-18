package sept.superfive.authmicroservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private boolean success;
    private String accessToken;
    private int userID;
    private Collection<? extends GrantedAuthority> roles;
}
