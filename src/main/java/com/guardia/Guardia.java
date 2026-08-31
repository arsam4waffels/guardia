package com.guardia;

import com.guardia.core.GuardiaContext;

public final class Guardia {
    private Guardia() {}
    // entry point
    public static <T> GuardiaContext<T> of(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object to validate cannot be null");
        }
        return new GuardiaContext<>(object);
    }
}