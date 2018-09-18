package com.jwtrestapi.beta.payload.response;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    public String accessToken;

    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
