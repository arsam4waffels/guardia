package com.guardia.validator;

import com.guardia.annotation.NotNull;
import com.guardia.core.ConstraintValidator;

public class NotNullValidator implements ConstraintValidator<NotNull, Object> {
    private String message;
    @Override
    public void initialize(NotNull annotation) {
        this.message = annotation.message();
    }
    @Override
    public boolean isValid(Object value) {
        return value != null;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
