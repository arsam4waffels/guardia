package com.guardia.annotation;

import com.guardia.core.Constraint;
import com.guardia.validator.RangeValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RangeValidator.class)
public @interface Range {
    long min() default Long.MIN_VALUE;
    long max() default Long.MAX_VALUE;
    String message() default "Value out of range";
}
