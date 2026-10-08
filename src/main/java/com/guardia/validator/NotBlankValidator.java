package com.guardia.validator;

import com.guardia.annotation.NotBlank;
import com.guardia.core.ConstraintValidator;

public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {

    private String message;

    @Override
    public void initialize(NotBlank annotation) {
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
