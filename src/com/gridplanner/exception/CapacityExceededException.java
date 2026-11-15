package com.gridplanner.exception;

public class CapacityExceededException extends GridException {
    public CapacityExceededException(String message) {
        super(message);
    }
}