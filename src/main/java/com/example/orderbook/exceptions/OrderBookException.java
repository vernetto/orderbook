package com.example.orderbook.exceptions;

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
