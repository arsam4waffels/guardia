package com.guardia.exception;

import java.util.List;
import java.util.Collections;

public class ValidationException extends RuntimeException {
    private final List<ValidationError> errors;
    public ValidationException(List<ValidationError> errors) {
        super(buildMessage(errors));
        this.errors = Collections.unmodifiableList(errors);
    }
    public List<ValidationError> getErrors() {
        return errors;
    }
    public boolean hasErrorForField(String fieldName) {
        return errors.stream()
                .anyMatch(e -> e.getFieldName().equals(fieldName));
    }
    private static String buildMessage(List<ValidationError> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation failed with ")
                .append(errors.size())
                .append(" error(s):\n");
        for (ValidationError error : errors) {
            sb.append("  → ").append(error).append("\n");
        }
        return sb.toString();
    }
}
