package com.guardia.validator;

import com.guardia.annotation.PositiveOrZero;
import com.guardia.core.ConstraintValidator;

public class PositiveOrZeroValidator implements ConstraintValidator<PositiveOrZero, Number> {

    private String message;

    @Override
    public void initialize(PositiveOrZero annotation) {
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(Number value) {
        if (value == null) return true;
        return value.doubleValue() >= 0;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
