package com.guardia.exception;

public class ValidationError {
    private final String fieldName;
    private final String message;
    private final Object rejectedValue;
    public ValidationError(String fieldName, String message, Object rejectedValue) {
        this.fieldName = fieldName;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }
    public String getFieldName() {
        return fieldName;
    }
    public String getMessage() {
        return message;
    }
    public Object getRejectedValue() {
        return rejectedValue;
    }
    @Override
    public String toString() {
        return String.format("[%s] %s (rejected: %s)",
                fieldName, message, rejectedValue);
    }
}