package com.example.orderbook.exceptions;

public enum ExceptionCode {
    ERR_001("Order book is closed"),
    ERR_002("no order book available"),
    ERR_003("cannot find Order"),
    ERR_004("Order book is closed"),
    ERR_005("Cannot edit open order"),
    ERR_006("PDF error");

    String errorReason;

    ExceptionCode(String errorReason) {
        this.errorReason = errorReason;
    }
}
