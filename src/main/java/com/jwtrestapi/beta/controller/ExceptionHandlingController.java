package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.exception.ErrorDetails;
import com.jwtrestapi.beta.exception.ResourceNotFoundException;
import com.jwtrestapi.beta.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import sun.security.pkcs.ParsingException;

import java.util.Date;

@ControllerAdvice
@RestController
public class ExceptionHandlingController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorDetails> resourceNotFound(ResourceNotFoundException ex, WebRequest req) {
        ErrorDetails errors = new ErrorDetails(new Date().getTime(), ex.getMessage(), req.getDescription(false), false);
        return new ResponseEntity<ErrorDetails>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorDetails> invalidInput(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        BindingResult bindingResult = ex.getBindingResult();
        sb.append("Error Count : " + bindingResult.getErrorCount() + "|");
        sb.append("Message : " + ValidationUtils.fromBindingErrors(bindingResult) + "|");
        sb.append("Target : " + bindingResult.getTarget());
        String messageError = sb.toString();
        ErrorDetails errors = new ErrorDetails(new Date().getTime(), "Validation Failed", messageError,false);
        return new ResponseEntity<ErrorDetails>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParsingException.class)
    public final ResponseEntity<ErrorDetails> handleParsingException(ParsingException ex, WebRequest req) {
        ErrorDetails errors = new ErrorDetails(new Date().getTime(),ex.getMessage(),req.getDescription(false),false);
        return new ResponseEntity<ErrorDetails>(errors,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
