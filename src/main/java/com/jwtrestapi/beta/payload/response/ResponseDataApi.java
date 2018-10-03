package com.jwtrestapi.beta.payload.response;

import lombok.Data;

@Data
public class ResponseDataApi {

    private String message;

    private Boolean success;

    private Object data;

    public ResponseDataApi(String message, Boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }
}
