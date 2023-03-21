package com.lsecotaro.tenpo.svcchallenge.advice;

import com.lsecotaro.tenpo.svcchallenge.exceptions.RateLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RateLimitAdvice {
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(RateLimitException.class)
    public void handler() {

    }
}
