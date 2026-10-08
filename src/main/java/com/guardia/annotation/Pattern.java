package com.guardia.annotation;

import com.guardia.core.Constraint;
import com.guardia.validator.PatternValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PatternValidator.class)
public @interface Pattern {
    String regex();
    String message() default "Value does not match the required pattern";
}
