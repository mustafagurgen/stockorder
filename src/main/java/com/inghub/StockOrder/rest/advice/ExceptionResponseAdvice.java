package com.inghub.StockOrder.rest.advice;

import com.inghub.StockOrder.exceptions.NotFoundException;
import com.inghub.StockOrder.exceptions.PermissionDeniedException;
import com.inghub.StockOrder.exceptions.StockOrderException;
import com.inghub.StockOrder.exceptions.UnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseAdvice {


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UnknownException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String unknownHandler(UnknownException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String permissionDeniedHandler(PermissionDeniedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(StockOrderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String defaultHandler(StockOrderException ex) {
        return ex.getMessage();
    }


}