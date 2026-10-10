package com.guardia.annotation;

import com.guardia.core.Constraint;
import com.guardia.validator.SizeValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SizeValidator.class)
public @interface Size {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "Size is outside the allowed bounds";
}
