package com.jwtrestapi.beta.util;

import com.jwtrestapi.beta.payload.ResponseService;
import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtils {

    public static List<String> fromBindingErrors(Errors errors) {
        List<String> validErros = new ArrayList<String>();
        for (ObjectError objectError:errors.getAllErrors()) {
            validErros.add(objectError.getDefaultMessage());
        }
        return validErros;
    }

    public static List<String> fromBindingErrorsClassPath(Errors errors) {
        List<String> validErros = new ArrayList<String>();
        for (ObjectError objectError:errors.getAllErrors()) {
            validErros.add(objectError.getCodes().getClass().getAnnotatedSuperclass().getClass().toString());
        }
        return validErros;
    }

    public static Boolean fieldValidation(BindingResult bindingResult, ResponseService responseService, Logger logger){

        logger.error(bindingResult.getAllErrors().toString());
        if(bindingResult.hasErrors()){
            responseService.setSuccess(false);
            responseService.setData("Format Data field "+bindingResult.getFieldError().getField()+" tidak benar "+"|"+ fromBindingErrors(bindingResult));
            logger.error("Format data field "+bindingResult.getFieldError().getField()+" tidak benar");
            return false;
        }
        return true;

    }
}
