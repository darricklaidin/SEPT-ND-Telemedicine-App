package com.sept.authmicroservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private boolean success;
    private String accessToken;
    private String roles = "User";

    public JwtAuthenticationResponse(boolean status, String accessToken) {
        this.success = status;
        this.accessToken = accessToken;
    }
}
