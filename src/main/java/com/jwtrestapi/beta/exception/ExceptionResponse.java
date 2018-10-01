package com.jwtrestapi.beta.exception;

import lombok.Data;

import java.util.List;

@Data
public class ExceptionResponse {
    private String errorCode;
    private String errorMessage;
    private List<String> errors;
    public ExceptionResponse() {
    }


    public ExceptionResponse(String errorCode, String errorMessage, List<String> errors) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }
}
