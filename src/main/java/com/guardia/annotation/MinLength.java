package com.guardia.annotation;

import com.guardia.core.Constraint;
import com.guardia.validator.MinLengthValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinLengthValidator.class)
public @interface MinLength {
    int value();
    String message() default "Field is too short";
}

