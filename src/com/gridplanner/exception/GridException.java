package com.gridplanner.exception;

public class GridException extends Exception {
    public GridException(String message) {
        super(message);
    }

    public GridException(String message, Throwable cause) {
        super(message, cause);
    }
}