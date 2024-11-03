package com.inghub.StockOrder.exceptions;

public class UnknownException extends RuntimeException {
    public UnknownException(String message) {
        super("Unknown exception! " + message);
    }
}