package com.gridplanner.interfaces;

import com.gridplanner.exception.GridValidationException;

public interface Validatable {
    void validate() throws GridValidationException;
}