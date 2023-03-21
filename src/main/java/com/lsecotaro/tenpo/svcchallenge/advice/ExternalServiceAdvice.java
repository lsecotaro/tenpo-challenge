package com.lsecotaro.tenpo.svcchallenge.advice;

import com.lsecotaro.tenpo.svcchallenge.exceptions.ExternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExternalServiceAdvice {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ExternalServiceException.class)
    public void handler() {

    }
}
