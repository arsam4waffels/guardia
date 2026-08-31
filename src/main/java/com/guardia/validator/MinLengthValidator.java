package com.guardia.validator;

import com.guardia.annotation.MinLength;
import com.guardia.core.ConstraintValidator;

public class MinLengthValidator implements ConstraintValidator<MinLength, String> {
    private int minLength;
    private String message;

    @Override
    public void initialize(MinLength annotation) {
        this.minLength = annotation.value();
        this.message = annotation.message();
    }
    @Override
    public boolean isValid(String value) {
        if (value == null) return true;
        return value.length() >= minLength;
    }
    @Override
    public String getMessage() {
        return message + " (min: " + minLength + ")";
    }
}
