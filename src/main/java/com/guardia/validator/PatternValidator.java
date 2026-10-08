package com.guardia.validator;

import com.guardia.annotation.Pattern;
import com.guardia.core.ConstraintValidator;

public class PatternValidator implements ConstraintValidator<Pattern, String> {

    private String regex;
    private String message;

    @Override
    public void initialize(Pattern annotation) {
        this.regex = annotation.regex();
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(String value) {
        if (value == null) return true;
        return value.matches(regex);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
