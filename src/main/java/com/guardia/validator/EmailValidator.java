package com.guardia.validator;

import com.guardia.annotation.Email;
import com.guardia.core.ConstraintValidator;

public class EmailValidator implements ConstraintValidator<Email, String> {
    private String message;
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    /*
    * -> valid email must have :
    * 1. something before the @
    * 2. the @
    * 3. something after the @
    * 4. dot
    * 5. domain
    * */
    @Override
    public void initialize(Email annotation) {
        this.message = annotation.message();
    }
    @Override
    public boolean isValid(String value) {
        if (value == null) return true;
        return value.matches(EMAIL_REGEX);
    }
    @Override
    public String getMessage() {
        return message;
    }
}