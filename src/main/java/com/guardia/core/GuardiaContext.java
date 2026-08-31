package com.guardia.core;

import com.guardia.exception.ValidationError;
import com.guardia.exception.ValidationException;

import java.util.List;

public class GuardiaContext<T> {
    private final T object;
    private List<ValidationError> errors;
    private boolean validated = false;
    public GuardiaContext(T object) {
        this.object = object;
    }
    public GuardiaContext<T> validate() {
        GuardiaEngine engine = new GuardiaEngine();
        this.errors = engine.collectErrors(object);
        this.validated = true;
        return this;
    }
    public GuardiaContext<T> throwIfInvalid() {
        ensureValidated();
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        return this;
    }
    public boolean isValid() {
        ensureValidated();
        return errors.isEmpty();
    }
    public List<ValidationError> getErrors() {
        ensureValidated();
        return errors;
    }
    public T get() {
        return object;
    }
    private void ensureValidated() {
        if (!validated) {
            throw new IllegalStateException(
                    "Call validate() before accessing results"
            );
        }
    }
}