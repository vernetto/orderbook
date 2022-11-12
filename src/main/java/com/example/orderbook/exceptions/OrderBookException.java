package com.example.orderbook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class OrderBookException extends Exception {
    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    final ExceptionCode exceptionCode;
    public OrderBookException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
