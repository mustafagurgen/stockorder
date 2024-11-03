package com.inghub.StockOrder.exceptions;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("Permission denied!");
    }
}