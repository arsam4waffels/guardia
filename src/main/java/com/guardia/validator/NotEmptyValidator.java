package com.guardia.validator;

import com.guardia.annotation.NotEmpty;
import com.guardia.core.ConstraintValidator;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {
    private String message;
    @Override
    public void initialize(NotEmpty annotation) {
        this.message = annotation.message();
    }
    @Override
    public boolean isValid(String value) {
        if (value == null) return false;
        return !value.isBlank();
    }
    @Override
    public String getMessage() {
        return message;
    }
}
