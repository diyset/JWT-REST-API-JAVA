package com.jwtrestapi.beta.payload.response;

import lombok.Data;

@Data
public class RestApiResponse {

    private Boolean success;

    private String message;

    private String data;

    public RestApiResponse(Boolean success, String message, String data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
