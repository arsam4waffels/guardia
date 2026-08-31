package com.guardia.core;

import java.lang.annotation.Annotation;

public interface ConstraintValidator<A extends Annotation, T> {
    void initialize(A annotation);
    boolean isValid(T value);
    String getMessage();
}
