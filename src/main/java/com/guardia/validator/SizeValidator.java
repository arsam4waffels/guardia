package com.guardia.validator;

import com.guardia.annotation.Size;
import com.guardia.core.ConstraintValidator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class SizeValidator implements ConstraintValidator<Size, Object> {

    private int min;
    private int max;
    private String message;

    @Override
    public void initialize(Size annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
        this.message = annotation.message();

        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid @Size bounds: min must be non-negative and max must be at least min");
        }
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) return true;

        int size;
        if (value instanceof CharSequence sequence) {
            size = sequence.length();
        } else if (value instanceof Collection<?> collection) {
            size = collection.size();
        } else if (value instanceof Map<?, ?> map) {
            size = map.size();
        } else if (value.getClass().isArray()) {
            size = Array.getLength(value);
        } else {
            throw new IllegalArgumentException(
                    "@Size supports CharSequence, Collection, Map, and array fields"
            );
        }

        return size >= min && size <= max;
    }

    @Override
    public String getMessage() {
        return message + " (min: " + min + ", max: " + max + ")";
    }
}
