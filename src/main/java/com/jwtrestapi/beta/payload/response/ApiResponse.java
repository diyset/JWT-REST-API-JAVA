package com.jwtrestapi.beta.payload.response;

import lombok.Data;

@Data
public class ApiResponse {

    private Boolean success;

    private String message;

    public ApiResponse() {
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
