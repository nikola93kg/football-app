package com.fifa.footballApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TeamNotFoundException.class})
    ProblemDetail handleNotFoundException(RuntimeException ex) {
        var problemDetail = ProblemDetail.forStatus( HttpStatus.NOT_FOUND.value() );
        problemDetail.setDetail( ex.getLocalizedMessage() );
        return problemDetail;
    }
}
