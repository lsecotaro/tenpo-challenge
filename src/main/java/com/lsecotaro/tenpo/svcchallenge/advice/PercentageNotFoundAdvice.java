package com.lsecotaro.tenpo.svcchallenge.advice;

import com.lsecotaro.tenpo.svcchallenge.exceptions.PercentageNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PercentageNotFoundAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PercentageNotFoundException.class)
    public void handler() {

    }
}
