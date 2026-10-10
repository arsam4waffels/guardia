package com.guardia.annotation;

import com.guardia.core.Constraint;
import com.guardia.validator.PositiveOrZeroValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PositiveOrZeroValidator.class)
public @interface PositiveOrZero {
    String message() default "Value must be zero or positive.";
}
