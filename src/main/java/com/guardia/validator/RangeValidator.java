package com.guardia.validator;

import com.guardia.annotation.Range;
import com.guardia.core.ConstraintValidator;

public class RangeValidator implements ConstraintValidator<Range, Number> {
    private long min;
    private long max;
    private String message;
    @Override
    public void initialize(Range annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
        this.message = annotation.message();
    }
    @Override
    public boolean isValid(Number value) {
        if (value == null) return true;
        long longValue = value.longValue();
        return longValue >= min && longValue <= max;
    }
    @Override
    public String getMessage() {
        return message + " (min: " + min + ", max: " + max + ")";
    }
}