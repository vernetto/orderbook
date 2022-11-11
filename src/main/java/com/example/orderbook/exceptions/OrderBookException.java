package com.example.orderbook.exceptions;

public class OrderBookException extends Exception {
    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    ExceptionCode exceptionCode;
    public OrderBookException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.setExceptionCode(exceptionCode);
    }
}
