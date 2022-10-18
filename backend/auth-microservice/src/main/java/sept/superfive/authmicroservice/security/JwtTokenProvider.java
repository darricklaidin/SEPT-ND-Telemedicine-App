package sept.superfive.authmicroservice.security;

import sept.superfive.authmicroservice.model.Role;
import sept.superfive.authmicroservice.model.RoleName;
import sept.superfive.authmicroservice.model.User;
import sept.superfive.authmicroservice.payload.UserDTO;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "authorities";

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userId = Integer.toString(user.getUserID());
        // set expiry time
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + SecurityConstant.EXPIRATION_TIME);

        String authorities = user.getRoles().stream().map(authority -> authority.getName().toString()).collect(Collectors.joining(","));

        // add claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("email", user.getEmail());
        claims.put(AUTHORITIES_KEY, authorities);

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstant.SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        UserDTO userDTO = getUserDtoFromToken(token);
        User principal = new User(userDTO.getUserID(), userDTO.getRoles());

        return new UsernamePasswordAuthenticationToken(principal, "", userDTO.getRoles());
    }

    public UserDTO getUserDtoFromToken(String token) {
        // Uses the SECRET key to convert JWT token to a Claims object which contains the body of the JWT
        Claims claims = Jwts.parser().setSigningKey(SecurityConstant.SECRET).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");

        List<Role> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(authority -> new Role(RoleName.valueOf(authority))).collect(Collectors.toList());

        return new UserDTO(Integer.parseInt(id), authorities);
    }
}
