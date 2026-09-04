package com.guardia.validator;

import com.guardia.annotation.Positive;
import com.guardia.core.ConstraintValidator;

public class PositiveValidator implements ConstraintValidator<Positive, Number> {
    private String message;
    @Override
    public void initialize(Positive annotation) {
        this.message = annotation.message();
    }
    @Override
    public boolean isValid(Number value) {
        if (value == null) return true;
        return value.doubleValue() > 0;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
