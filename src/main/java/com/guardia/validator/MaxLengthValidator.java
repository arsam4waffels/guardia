package com.guardia.validator;

import com.guardia.annotation.MaxLength;
import com.guardia.core.ConstraintValidator;

public class MaxLengthValidator implements ConstraintValidator<MaxLength, String> {
    private int maxLength;
    private String message;
    @Override
    public void initialize(MaxLength annotation) {
        this.maxLength = annotation.value();
        this.message = annotation.message();
    }
    @Override
    public boolean isValid(String value) {
        if (value == null) return true;
        return value.length() <= maxLength;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
