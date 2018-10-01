package com.jwtrestapi.beta.exception;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ErrorDetails {
    private Long timestamp;
    private String message;
    private String details;
    private boolean success;

}
